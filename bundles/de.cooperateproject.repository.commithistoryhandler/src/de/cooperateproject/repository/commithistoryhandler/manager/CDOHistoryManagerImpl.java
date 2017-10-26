package de.cooperateproject.repository.commithistoryhandler.manager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.Range;
import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.commit.CDOCommitInfo;
import org.eclipse.emf.cdo.common.commit.CDOCommitInfoManager;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.revision.CDOIDAndVersion;
import org.eclipse.emf.cdo.common.revision.CDORevisionKey;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.io.IOUtil;

import de.cooperateproject.repository.commithistoryhandler.dto.Commit;
import de.cooperateproject.repository.commithistoryhandler.dto.CrossReference;

public class CDOHistoryManagerImpl implements Closeable, ICDOHistoryManager {

	private interface Initializer {
		void init() throws IOException;
	}

	private static final Logger LOGGER = Logger.getLogger(CDOHistoryManagerImpl.class);
	private static final String CACHE_NAME = "commithistory";
	private final Initializer initializer;
	private final String repoName;
	private SortedSerializableCache<Long, Commit> cache;
	private CDOSession session;
	private CDOBranch relevantBranch;

	public CDOHistoryManagerImpl(String connectionString, String repoName, String dataLocation) {
		this.repoName = repoName;
		cache = createCache(dataLocation, CACHE_NAME);
		initializer = () -> init(connectionString, repoName);
	}

	private SortedSerializableCache<Long, Commit> createCache(String dataLocation, String cacheName) {
		File cacheLocation = new File(Optional.of(dataLocation).orElse("../" + repoName) + "_" + cacheName);
		return new SortedSerializableCache<>(cacheLocation, (l1, l2) -> (int) (l1 - l2));
	}

	public String getRepositoryName() {
		return repoName;
	}

	public void start() throws IOException {
		LOGGER.info(String.format("Starting listening for CDOCommitInfo in %s", repoName));
		initializer.init();
	}

	@Override
	public void close() {
		IOUtil.closeSilent(session);
	}

	private void init(String connectionString, String repoName) throws IOException {
		try {
			session = openSession(connectionString, repoName);
			relevantBranch = session.getBranchManager().getMainBranch();
			registerCDOChangeListener(session);
			catchUp(session);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private void registerCDOChangeListener(CDOSession session) {
		session.getCommitInfoManager().addCommitInfoHandler(commitInfo -> {
			if (commitInfo.getBranch() == relevantBranch) {
				addToCache(commitInfo);
			}
		});
	}

	private void catchUp(CDOSession session) {
		LOGGER.debug(String.format("Catching up commits for %s", repoName));
		long mostRecentTimestampInData = Optional.of(cache.getKeysSorted().descendingIterator())
				.filter(Iterator::hasNext).map(Iterator::next).orElse(CDOBranch.UNSPECIFIED_DATE);
		long mostRecentTimestampInRepo = Long.MAX_VALUE;
		
		CDOCommitInfoManager commitInfoManager = session.getCommitInfoManager();
		commitInfoManager.getCommitInfos(relevantBranch, mostRecentTimestampInData, mostRecentTimestampInRepo, this::addToCache);
	}

	private void addToCache(CDOCommitInfo commitInfo) {
		if (cache.containsKey(commitInfo.getTimeStamp())) {
			return;
		}
		
		LOGGER.debug(String.format("Creating new cache entry for %s with timestamp %d", repoName, commitInfo.getTimeStamp()));
		
		CDOView historicView = session.openView(commitInfo.getBranch(), commitInfo.getTimeStamp());
		try {
			Commit commit = new Commit();
			commit.setTimestamp(commitInfo.getTimeStamp());

			// changed objects
			Set<CDOObject> changedObjects = Stream
					.concat(commitInfo.getChangedObjects().stream().map(CDORevisionKey::getID),
							commitInfo.getNewObjects().stream().map(CDOIDAndVersion::getID))
					.map(historicView::getObject).collect(Collectors.toSet());
			commit.getChangedObjects().addAll(createIdList(changedObjects));

			// changed resources
			Collection<CDOObject> changedResources = changedObjects.stream().map(CDOObject::cdoResource)
					.filter(Objects::nonNull).collect(Collectors.toSet());
			commit.getChangedResources().addAll(createIdList(changedResources));

			// affected cross references
			Set<CrossReference> changedCrossReferences = changedObjects.stream().map(historicView::queryXRefs)
					.flatMap(Collection::stream).map(CrossReference::create).collect(Collectors.toSet());
			changedCrossReferences.forEach(ref -> ref.setCommit(commit));
			commit.getCrossReferences().addAll(changedCrossReferences);

			CDOView previousView = session.openView(commitInfo.getBranch(), commitInfo.getPreviousTimeStamp());
			try {
				Set<CDOObject> deletedObjects = commitInfo.getDetachedObjects().stream().map(CDOIDAndVersion::getID)
						.map(previousView::getObject).collect(Collectors.toSet());
				commit.getDeletedObjects().addAll(createIdList(deletedObjects));

				// changed resources
				Set<CDOObject> resourcesOfDeletedObjects = deletedObjects.stream().map(CDOObject::cdoResource)
						.collect(Collectors.toSet());
				commit.getChangedResources().addAll(createIdList(resourcesOfDeletedObjects));

			} finally {
				IOUtil.closeSilent(previousView);
			}

			cache.put(commit.getTimestamp(), commit);
		} finally {
			IOUtil.closeSilent(historicView);
		}

	}

	private static Collection<CDOID> createIdList(Collection<CDOObject> cdoElements) {
		return cdoElements.stream().filter(Objects::nonNull).map(CDOObject::cdoID).collect(Collectors.toSet());
	}

	private static CDOSession openSession(String connectionString, String repo) {
		IConnector connector = Net4jUtil.getConnector(IPluginContainer.INSTANCE, connectionString);
		CDONet4jSessionConfiguration sessionConfig = CDONet4jUtil.createNet4jSessionConfiguration();
		sessionConfig.setConnector(connector);
		sessionConfig.setRepositoryName(repo);
		return sessionConfig.openNet4jSession();
	}

	@Override
	public Collection<Long> getChangedTimestamps(Collection<CDOID> requestedResources,
			Collection<CDOID> crossReferencedResources, Range<Long> timeRange) {
		Predicate<Commit> predicate = createPredicate(requestedResources, crossReferencedResources,
				Optional.ofNullable(timeRange));
		return getChangedTimestamps(predicate);
	}

	private Collection<Long> getChangedTimestamps(Predicate<Commit> filter) {
		return cache.values().stream().filter(filter).map(Commit::getTimestamp).sorted().collect(Collectors.toList());
	}

	private Predicate<Commit> createPredicate(Collection<CDOID> requestedResources, Collection<CDOID> crossReferencedResources, Optional<Range<Long>> timeRange) {
		return commit -> 
			timeRange.map(tr -> tr.contains(commit.getTimestamp())).orElse(true) &&
			(
				commit.getChangedResources().stream().anyMatch(requestedResources::contains) ||
				commit.getCrossReferences().stream().anyMatch(cr -> requestedResources.contains(cr.getFromResource()) && crossReferencedResources.contains(cr.getToResource()))
			);
	}
}

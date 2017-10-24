package de.cooperateproject.repository.commithistoryhandler.manager;

import java.util.Collection;

import org.apache.commons.lang3.Range;
import org.eclipse.emf.cdo.common.id.CDOID;

public interface ICDOHistoryManager {

	Collection<Long> getChangedTimestamps(Collection<CDOID> requestedResources,
			Collection<CDOID> crossReferencedResources, Range<Long> timeRange);

}

package de.cooperateproject.repository.commithistoryhandler.manager;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.eclipse.net4j.util.StringUtil;
import org.eclipse.net4j.util.io.IOUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public enum CDOHistoryManagerAggregator {

	INSTANCE;

	private static final Logger LOGGER = Logger.getLogger(CDOHistoryManagerAggregator.class);
	private static final String WILDCARD_IP = "0.0.0.0";
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 2036;
	private static final int CONNECTION_TIMEOUT_IN_SECONDS = 20;
	private Map<String, CDOHistoryManagerImpl> repositoryListeners;
	private String connectionString;
	
	public void init(File configFile) throws IOException {
		Document document = parseCDOConfig(configFile);
		connectionString = determineConnectionString(document.getElementsByTagName("acceptor"));
		Collection<Pair<String, String>> repositoryInformation = determineRepositories(document.getElementsByTagName("repository"));
		repositoryListeners = repositoryInformation.stream().map(ri -> new CDOHistoryManagerImpl(connectionString, ri.getKey(), ri.getValue())).collect(Collectors.toMap(CDOHistoryManagerImpl::getRepositoryName, o -> o));
	}

	public void start() throws IOException {
		try {
			LOGGER.info(String.format("Trying to connecto %s.", connectionString));
			waitForReachability(URI.create(connectionString), CONNECTION_TIMEOUT_IN_SECONDS);			
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
		for (CDOHistoryManagerImpl repositoryListener : repositoryListeners.values()) {
			repositoryListener.start();		
		}
	}
	
	public void stop() {
		repositoryListeners.values().forEach(CDOHistoryManagerImpl::close);
	}

	private static Document parseCDOConfig(File configFile) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(configFile);			
		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}
	}
	
	private static Collection<Pair<String, String>> determineRepositories(NodeList repositories) {
		Collection<Pair<String, String>> result = new ArrayList<>();
		for (int i = 0; i < repositories.getLength(); i++) {
			Element repositoryConfig = (Element)repositories.item(i);
			String repoName = repositoryConfig.getAttribute("name");
			NodeList dbAdapters = repositoryConfig.getElementsByTagName("dbAdapter");
			NodeList dataSources = repositoryConfig.getElementsByTagName("dataSource");
			String location = null;
			if (dbAdapters.getLength() == 1 && "h2".equals(((Element)dbAdapters.item(0)).getAttribute("name")) && dataSources.getLength() == 1) {
				String jdbcUrl = ((Element)dataSources.item(0)).getAttribute("URL");
				if (jdbcUrl.startsWith("jdbc:h2:") && !jdbcUrl.startsWith("jdbc:h2:mem:")) {
					location = jdbcUrl.substring("jdbc:h2:".length());
				}
			}
			result.add(Pair.of(repoName, location));
		}
		return result;
	}

	private static String determineConnectionString(NodeList acceptors) {
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		for (int i = 0; i < acceptors.getLength(); i++) {
			Element acceptorConfig = (Element) acceptors.item(i);
			if (hasNegotiator(acceptorConfig) || !"tcp".equals(acceptorConfig.getAttribute("type"))) {
				continue;
			}
			String listenAddr = acceptorConfig.getAttribute("listenAddr");
			if (StringUtil.isEmpty(listenAddr) || WILDCARD_IP.equals(listenAddr)) {
				listenAddr = DEFAULT_HOST;
			}
			String portString = acceptorConfig.getAttribute("port");
			int listenPort;
			try {
				listenPort = Integer.parseInt(portString);
			} catch (NumberFormatException e) {
				continue;
			}
			host = listenAddr;
			port = listenPort;
			break;
		}
		
		return String.format("tcp://%s:%d", host, port);
	}

	private static boolean hasNegotiator(Element acceptorConfig) {
		for (int i = 0; i < acceptorConfig.getChildNodes().getLength(); i++) {
			Node acceptorChild = acceptorConfig.getChildNodes().item(i);
			if ("negotiator".equals(acceptorChild.getNodeName())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("squid:S2095")
	private static void waitForReachability(URI connectionURI, int timeoutInSeconds) throws TimeoutException {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < (timeoutInSeconds * 1000)) {
			Socket socket = new Socket();
			try {
				socket.connect(
						new InetSocketAddress(InetAddress.getByName(connectionURI.getHost()), connectionURI.getPort()),
						1000);
				return;
			} catch (IOException e) {
				continue;
			} finally {
				IOUtil.closeSilent(socket);
			}
		}
		throw new TimeoutException();
	}

	public Optional<ICDOHistoryManager> getHistoryManager(String repositoryID) {
		return Optional.ofNullable(repositoryListeners.getOrDefault(repositoryID, null));
	}

}

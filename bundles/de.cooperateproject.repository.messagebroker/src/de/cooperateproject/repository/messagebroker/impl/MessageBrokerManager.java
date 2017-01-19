package de.cooperateproject.repository.messagebroker.impl;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.util.LoggingBrokerPlugin;
import org.apache.activemq.broker.util.TimeStampingBrokerPlugin;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

public enum MessageBrokerManager {

	INSTANCE;

	public static MessageBrokerManager getInstance() {
		return INSTANCE;
	}

	private BrokerService brokerInstance;

	public void startBroker() throws MessageBrokerManagementException {
		try {
			if (brokerInstance == null) {
				String host = MessageBrokerConfiguration.getInstance().getHost();
				int port = MessageBrokerConfiguration.getInstance().getPort();
				BrokerService broker = createBroker("CooperateRepositoryMessageBroker", host, port);
				brokerInstance = broker;
			}
			if (!brokerInstance.isStarted()) {
				brokerInstance.start();
			}
		} catch (Exception e) {
			throw new MessageBrokerManagementException("Could not initialize and start message broker.", e);
		}
	}

	public void stopBroker() throws MessageBrokerManagementException {
		try {			
			if (brokerInstance != null && brokerInstance.isStarted()) {
				if (!brokerInstance.isStopping()) {
					brokerInstance.stop();
				}
				brokerInstance.waitUntilStopped();
			}
		} catch (Exception e) {
			throw new MessageBrokerManagementException("Could not stop message broker.", e);
		}
	}

	private static BrokerService createBroker(String name, String host, int port) throws Exception {
		BrokerService broker = new BrokerService();
		broker.setPlugins(getPlugins());
		broker.setBrokerName(name);
		broker.addConnector(String.format("tcp://%s:%d", host, port));
		broker.setUseShutdownHook(true);
		broker.setUseJmx(true);
		broker.setPersistent(false);
		return broker;
	}
	
	private static BrokerPlugin[] getPlugins() {
		LoggingBrokerPlugin logging = new LoggingBrokerPlugin();
		logging.setLogAll(true);
		
		TimeStampingBrokerPlugin timestamping = new TimeStampingBrokerPlugin();
		
		SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
		authentication.setAnonymousAccessAllowed(true);
		
		return new BrokerPlugin[]{logging, timestamping, authentication};		
	}

}

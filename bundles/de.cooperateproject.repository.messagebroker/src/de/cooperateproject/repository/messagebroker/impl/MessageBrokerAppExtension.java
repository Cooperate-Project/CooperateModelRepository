package de.cooperateproject.repository.messagebroker.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.spi.server.IAppExtension;

import de.cooperateproject.repository.logging.configurator.LoggingConfigurator;
import de.cooperateproject.repository.messagebroker.Activator;

public class MessageBrokerAppExtension implements IAppExtension {

	private static final Logger LOGGER = Logger.getLogger(MessageBrokerAppExtension.class);
	private LoggingConfigurator logConfig = new ActiveMQLoggingConfigurator();
	
	@Override
	public void start(File configFile) throws Exception {
		try {
			logConfig.configureLoggingSync(Activator.getDefault());
			LOGGER.info("Starting message broker.");
			MessageBrokerManager.getInstance().startBroker();
		} catch (MessageBrokerManagementException e) {
			LOGGER.error("Could not start message broker.", e);
		}
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Stoping message broker.");
		MessageBrokerManager.getInstance().stopBroker();
		logConfig.unconfigureLogging();
	}

}

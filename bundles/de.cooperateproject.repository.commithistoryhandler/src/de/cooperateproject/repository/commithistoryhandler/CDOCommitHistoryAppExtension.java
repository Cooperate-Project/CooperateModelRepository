package de.cooperateproject.repository.commithistoryhandler;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.spi.server.IAppExtension;

import de.cooperateproject.repository.commithistoryhandler.manager.CDOHistoryManagerAggregator;
import de.cooperateproject.repository.logging.configurator.LoggingConfigurator;

public class CDOCommitHistoryAppExtension implements IAppExtension {

	private static final Logger LOGGER = Logger.getLogger(CDOCommitHistoryAppExtension.class);
	private LoggingConfigurator logConfig = new LoggingConfigurator();
	
	@Override
	public void start(File configFile) throws Exception {
		logConfig.configureLoggingSync(Activator.getDefault());
		LOGGER.info(String.format("Starting %s", CDOCommitHistoryAppExtension.class.getSimpleName()));
		CDOHistoryManagerAggregator.INSTANCE.init(configFile);
		new Thread(() -> {
			try {
				CDOHistoryManagerAggregator.INSTANCE.start();
			} catch (IOException e) {
				LOGGER.error("Starting of repository listeners failed.");
			}
		}).start();
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info(String.format("Stopping %s", CDOCommitHistoryAppExtension.class.getSimpleName()));
		CDOHistoryManagerAggregator.INSTANCE.stop();
		logConfig.unconfigureLogging();
	}

}

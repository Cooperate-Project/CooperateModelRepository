package de.cooperateproject.repository.commithistoryhandler;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.cdo.spi.server.IAppExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cooperateproject.repository.commithistoryhandler.manager.CDOHistoryManagerAggregator;

public class CDOCommitHistoryAppExtension implements IAppExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDOCommitHistoryAppExtension.class);

	@Override
	public void start(File configFile) throws Exception {
		LOGGER.info("Starting {}", CDOCommitHistoryAppExtension.class.getSimpleName());
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
		LOGGER.info("Stopping {}", CDOCommitHistoryAppExtension.class.getSimpleName());
		CDOHistoryManagerAggregator.INSTANCE.stop();
	}

}

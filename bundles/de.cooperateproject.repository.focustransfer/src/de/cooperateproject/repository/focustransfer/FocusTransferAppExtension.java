package de.cooperateproject.repository.focustransfer;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.spi.server.IAppExtension;

import de.cooperateproject.repository.focustransfer.protocol.FocusTransferProtocolFactory;
import de.cooperateproject.repository.focustransfer.protocol.FocusTransferSharedInfrastructure;
import de.cooperateproject.repository.logging.configurator.LoggingConfigurator;

/**
 * Net4j extension for the focus transfer protocol. 
 */
public class FocusTransferAppExtension implements IAppExtension {

	private static final Logger LOGGER = Logger.getLogger(FocusTransferAppExtension.class);
	private LoggingConfigurator logConfig = new LoggingConfigurator();

	@Override
	public void start(File configFile) throws Exception {
		logConfig.configureLoggingSync(Activator.getInstance());
		LOGGER.info(String.format("Starting %s", FocusTransferAppExtension.class.getSimpleName()));
		FocusTransferProtocolFactory.setAndActivateSharedInfrastructure(new FocusTransferSharedInfrastructure());
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info(String.format("Stopping %s", FocusTransferAppExtension.class.getSimpleName()));
		FocusTransferProtocolFactory.setAndActivateSharedInfrastructure(null);
		logConfig.unconfigureLogging();
	}
	
	

}

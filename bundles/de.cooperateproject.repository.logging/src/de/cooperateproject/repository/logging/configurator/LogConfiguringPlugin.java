package de.cooperateproject.repository.logging.configurator;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class LogConfiguringPlugin extends Plugin {

	private LoggingConfigurator logConfig = new LoggingConfigurator();

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		logConfig.configureLoggingAsync(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logConfig.unconfigureLogging();
		super.stop(context);
	}
	
}

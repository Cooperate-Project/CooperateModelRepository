package de.cooperateproject.repository.logging.configurator;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import de.cooperateproject.repository.logging.configurator.impl.BundleLoggingAppender;

public class LoggingConfigurator {

	public void configureLoggingAsync(Plugin plugin) throws InterruptedException {
		new Thread(() -> {
			try {
				configureLoggingSync(plugin);
			} catch (InterruptedException e) {
				// ignore
				return;
			}
		}).start();
	}
	
	public void configureLoggingSync(Plugin plugin) throws InterruptedException {
		ILog logger = getBundleLogger(plugin);
		configureLogging(logger);
	}
	
	public void unconfigureLogging() {
		BasicConfigurator.resetConfiguration();
	}

	protected void configureLogging(ILog logger) {
		Appender logAppender = new BundleLoggingAppender(logger);
		configureLogging(logger.getBundle().getSymbolicName(), logAppender);
	}
	
	protected void configureLogging(String loggerName, Appender logAppender) {
		Logger bundleLogger = Logger.getLogger(loggerName);
		bundleLogger.addAppender(logAppender);
		bundleLogger.setLevel(Level.DEBUG);
	}
	
	protected ILog getBundleLogger(Plugin plugin) throws InterruptedException {
		ILog bundleLogger = null;
		while(bundleLogger == null) {
			try {
				bundleLogger = plugin.getLog();				
			} catch (Exception e) {
				Thread.sleep(100);
			}
		}
		return bundleLogger;
	}
	
}

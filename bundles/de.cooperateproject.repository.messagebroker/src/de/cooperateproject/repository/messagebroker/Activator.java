package de.cooperateproject.repository.messagebroker;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import de.cooperateproject.repository.messagebroker.impl.MessageBrokerManagementException;
import de.cooperateproject.repository.messagebroker.impl.MessageBrokerManager;

public class Activator extends Plugin {

	public static final String PLUGIN_ID = "de.cooperateproject.repository.messagebroker";
	private static final Logger LOGGER = Logger.getLogger(Activator.class);
	private BundleListener bundleListener;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		bundleListener = this::handleBundleEvent;
		context.addBundleListener(bundleListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		LOGGER.info("Stoping message broker.");
		MessageBrokerManager.getInstance().stopBroker();
		context.removeBundleListener(bundleListener);
		super.stop(context);
	}

	private void handleBundleEvent(BundleEvent event) {
		if (event.getBundle() == this.getBundle() && event.getType() == BundleEvent.STARTED) {
			new Thread(() -> {
				configureLoggingSafe();
				startMessageBroker();
			}).start();
		}
	}
	
	private void configureLoggingSafe() {
		try {
			Thread.sleep(4000);
			configureLogging();							
		} catch (NullPointerException e) {
			LOGGER.error("Could not configure logger because of NPE in framework.", e);
		} catch (InterruptedException e) {
			LOGGER.error("Was not able to wait for configuring the logging. Aborting.", e);
			Thread.currentThread().interrupt();
		}
	}

	private void configureLogging() {
		Appender logAppender = new BundleLoggingAppender(getLog());
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure(logAppender);
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	private static void startMessageBroker() {
		try {
			LOGGER.info("Starting message broker.");
			MessageBrokerManager.getInstance().startBroker();
		} catch (MessageBrokerManagementException e) {
			LOGGER.error("Could not start message broker.", e);
		}
	}
}

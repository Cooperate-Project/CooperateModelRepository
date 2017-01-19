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
	private final BundleListener bundleListener = this::handleBundleEvent;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
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
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						configureLogging();
						startMessageBroker();
					} catch (InterruptedException e) {
						return;
					}
				}
			}).start();
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

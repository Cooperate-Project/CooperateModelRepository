package de.cooperateproject.repository.messagebroker;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

	private static Plugin instance;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setDefault(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		setDefault(null);
		super.stop(context);
	}

	private static void setDefault(Plugin plugin) {
		Activator.instance = plugin;
	}
	
	public static Plugin getDefault() {
		return instance;
	}
	
}

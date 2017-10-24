package de.cooperateproject.repository.commithistoryhandler;

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
	
	private void setDefault(Plugin instance) {
		Activator.instance = instance;
	}
	
	public static Plugin getDefault() {
		return Activator.instance;
	}

}

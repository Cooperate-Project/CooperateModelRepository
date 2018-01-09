package de.cooperateproject.repository.focustransfer;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Activator of the bundle.
 */
public class Activator extends Plugin {

	private static Activator instance;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setInstance(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		setInstance(null);
		super.stop(context);
	}

	/**
	 * @return The bundle instance.
	 */
	public static Activator getInstance() {
		return instance;
	}
	
	private static void setInstance(Activator activator) {
		Activator.instance = activator;
	}

}

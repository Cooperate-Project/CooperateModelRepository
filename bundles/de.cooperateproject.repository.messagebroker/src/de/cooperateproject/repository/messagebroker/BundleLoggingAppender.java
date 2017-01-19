package de.cooperateproject.repository.messagebroker;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BundleLoggingAppender extends AppenderSkeleton {

	private final ILog bundleLogging;

	public BundleLoggingAppender(ILog bundleLogging) {
		this.bundleLogging = bundleLogging;
	}

	@Override
	public void close() {
		return;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent arg0) {
		Exception t = null;
		if (arg0.getThrowableInformation() != null && arg0.getThrowableInformation().getThrowable() instanceof Exception) {			
			t = (Exception)arg0.getThrowableInformation().getThrowable();
		}
		IStatus status = new Status(getSeverity(arg0.getLevel()), Activator.PLUGIN_ID, arg0.getRenderedMessage(), t);
		bundleLogging.log(status);
	}

	private static int getSeverity(Level l) {
		switch (l.toInt()) {
		case Level.ERROR_INT:
			return IStatus.ERROR;

		case Level.WARN_INT:
			return IStatus.WARNING;
		}
		return IStatus.INFO;
	}

}

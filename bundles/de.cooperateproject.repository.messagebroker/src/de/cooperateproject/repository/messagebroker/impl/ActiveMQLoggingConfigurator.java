package de.cooperateproject.repository.messagebroker.impl;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.cooperateproject.repository.logging.configurator.LoggingConfigurator;

public class ActiveMQLoggingConfigurator extends LoggingConfigurator {

	@Override
	protected void configureLogging(String loggerName, Appender logAppender) {
		super.configureLogging(loggerName, logAppender);
		Logger activemqLogger = Logger.getLogger("org.apache.activemq");
		activemqLogger.addAppender(logAppender);
		activemqLogger.setLevel(Level.DEBUG);
	}

}

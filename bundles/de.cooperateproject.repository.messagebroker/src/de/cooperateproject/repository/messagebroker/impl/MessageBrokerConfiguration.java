package de.cooperateproject.repository.messagebroker.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public enum MessageBrokerConfiguration {
	
	INSTANCE;
	
	public static MessageBrokerConfiguration getInstance() {
		return INSTANCE;
	}
	
	private final Properties properties;
	
	private MessageBrokerConfiguration() {
		properties = createProperties(getConfigurationFile());
	}
	
	public String getHost() {
		return properties.getProperty(MessageBrokerConfigurationProperties.HOST.getKey());
	}
	
	public int getPort() {
		String portString = properties.getProperty(MessageBrokerConfigurationProperties.PORT.getKey());
		try {
			return Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			return (int)MessageBrokerConfigurationProperties.PORT.getDefault();
		}
	}
	
	private static Properties createProperties(File configurationFile) {
		Properties defaultProperties = createDefaultProperties();
		Properties properties = new Properties(defaultProperties);
		try {
			InputStream is = new FileInputStream(configurationFile);
			try {
				properties.load(is);
			} finally {
				IOUtils.closeQuietly(is);
			}			
		} catch (IOException e) {
			try {
				save(defaultProperties, configurationFile);
			} catch (IOException e1) {
				Logger.getLogger(MessageBrokerConfiguration.class).error("Could not save properties.", e1);
			}
		}
		return properties;
	}

	private static void save(Properties properties2, File configurationFile) throws IOException {
		OutputStream os = new FileOutputStream(configurationFile);
		try {
			properties2.store(os, "Automatically saved.");
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	private static Properties createDefaultProperties() {
		Properties properties = new Properties();
		Arrays.stream(MessageBrokerConfigurationProperties.values()).forEach(p -> properties.put(p.getKey(), p.getDefaultAsString()));
		return properties;
	}
	
	private static File getConfigurationFile() {
		String configurationFolder = System.getProperty("net4j.config");
		if (StringUtils.isEmpty(configurationFolder)) {
			configurationFolder = "configuration";
		}
		File configFolder = new File(configurationFolder);
		return new File(configFolder, "messagebroker.properties");
	}
	
}

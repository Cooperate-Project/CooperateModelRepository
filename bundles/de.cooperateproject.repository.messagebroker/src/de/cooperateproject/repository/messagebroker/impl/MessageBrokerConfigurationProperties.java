package de.cooperateproject.repository.messagebroker.impl;

public enum MessageBrokerConfigurationProperties {
	HOST("host", "0.0.0.0"),
	PORT("port", 61616);
	
	private final String key;
	private final Object value;
	
	private MessageBrokerConfigurationProperties(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getDefault() {
		return value;
	}
	
	public String getDefaultAsString() {
		return value.toString();
	}
	
}

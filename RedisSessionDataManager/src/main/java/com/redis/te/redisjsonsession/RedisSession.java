package com.redis.te.redisjsonsession;

import redis.clients.jedis.JedisPooled;


public class RedisSession {
	// Configure the session
	private String sessionNamespace  = "session:";
	String host;
	int port;
	long inactiveInterval;
	boolean autoExpire = false;
	
	// Jedis client
	private JedisPooled client;
	
	RedisSession(String theHost, int thePort, long theInactiveInterval, boolean theAutoexpire) {
		this.host = theHost;
		this.port = thePort;
		this.inactiveInterval = theInactiveInterval;
		this.autoExpire = theAutoexpire;
		
        //Disable JVM DNS caching.
        java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
		
		client = new JedisPooled(this.host, this.port);
	}
	
	public boolean isAutoExpire() {
		return autoExpire;
	}

	public void setAutoExpire(boolean autoExpire) {
		this.autoExpire = autoExpire;
	}

	public String getSessionNamespace() {
		return sessionNamespace;
	}

	public void setSessionNamespace(String sessionNamespace) {
		this.sessionNamespace = sessionNamespace;
	}
	
	JedisPooled getRedisConnection() {
		return this.client;
	}

	public long getMaxInactiveInterval() {
		return inactiveInterval;
	}

	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.inactiveInterval = maxInactiveInterval;
	}
}

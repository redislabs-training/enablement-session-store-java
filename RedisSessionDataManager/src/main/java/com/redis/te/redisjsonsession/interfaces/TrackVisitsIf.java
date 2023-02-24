package com.redis.te.redisjsonsession.interfaces;

import redis.clients.jedis.JedisPooled;

public interface TrackVisitsIf {
	JedisPooled getDbConnection();
	String getSessionKey();

	
	default public void addVisited(String url) {
		getDbConnection().pfadd(getLocalKey(), url);
		return;
	}
	
	default public long countVisited() {
		return getDbConnection().pfcount(getLocalKey());
	}
	
	default String getLocalKey() {
		return getSessionKey() + ":" + "trackvisits";
	}
	
	default void expire(long inactiveInterval) {
		getDbConnection().expire(getLocalKey(), inactiveInterval);
	}
}

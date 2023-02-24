package com.redis.te.redisjsonsession.interfaces;

import redis.clients.jedis.JedisPooled;

public interface HistoryListIf {
	JedisPooled getDbConnection();
	String getSessionKey();
	
	default public void historyAppendUrl(String url) {
		getDbConnection().rpush(getHistoryKey(), url);
	}
	
	default String getHistoryKey() {
		return getSessionKey() + ":" + "history";
	}
	
	default void expire(long inactiveInterval) {
		getDbConnection().expire(getHistoryKey(), inactiveInterval);
	}
}

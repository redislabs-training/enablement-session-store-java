package com.redis.te.redisjsonsession.interfaces;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;

public interface GeoLocationIf {
	JedisPooled getDbConnection();
	String getSessionKey();
	
	default public void geoSetLocation(String theCoordinates) {
		getDbConnection().jsonSet(getSessionKey(), new Path("location"), theCoordinates);
	}
	
}

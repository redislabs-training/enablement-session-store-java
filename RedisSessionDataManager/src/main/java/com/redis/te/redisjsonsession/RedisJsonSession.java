package com.redis.te.redisjsonsession;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.te.redisjsonsession.interfaces.GeoLocationIf;
import com.redis.te.redisjsonsession.interfaces.HistoryListIf;
import com.redis.te.redisjsonsession.interfaces.ShoppingCartIf;
import com.redis.te.redisjsonsession.interfaces.TrackVisitsIf;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.json.Path;


@Component("RedisJsonSession")
public class RedisJsonSession extends RedisSession implements ShoppingCartIf, TrackVisitsIf, HistoryListIf, GeoLocationIf{
	private String sessionID;

	RedisJsonSession(String theHost, int thePort, long theInactiveInterval, boolean theAutoexpire) {
		super(theHost, thePort, theInactiveInterval, theAutoexpire);
	}

	String createSession() throws JsonProcessingException {
		// Generate a session UUID and store it
        this.sessionID = UUID.randomUUID().toString();
        
        // 1. The session is a multi-data structures one
        // 2. Metadata is stored in SessionDetails
        // 3. Metadata is stored in a JSON e.g. session:14d60992-c52f-4178-9b63-5e7d88656609
        // 4. Additional data may be store in the JSON e.g. the shopping cart
        SessionDetails newSession = new SessionDetails();
        newSession.getLastAccessedTime();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonSession = objectMapper.writeValueAsString(newSession);
        
        // JSON data structure is created
        super.getRedisConnection().jsonSet(getSessionKey(), jsonSession);
        
        // And expiration time is set accordingly
        super.getRedisConnection().expire(getSessionKey(), super.getMaxInactiveInterval());
        
        // Initializations
        shoppingCartInit();
      
		return this.sessionID;
	}
	
	void loadSession(String sessionID) {
		System.out.println("loading new session");
		this.sessionID = sessionID;
	}

	@Override
	public JedisPooled getDbConnection() {
		// Autoexpire the session when there is activity
		// Retrieving a database connection is sign of session activity
		if (this.isAutoExpire()){
			this.expireAll(getMaxInactiveInterval());
		}
		return getRedisConnection();
	}
	
	public void setAttribute(String theKey, String theValue) {
		super.getRedisConnection().jsonSet(getSessionKey(), new Path(theKey), theValue);
	}
	
	public String getAttribute(String theKey) {
		try {
			return (String) super.getRedisConnection().jsonGet(getSessionKey(), new Path(theKey));
		}
		catch(JedisDataException e) {
			return null;
		}
	}

	@Override
	public String getSessionKey() {
		return super.getSessionNamespace() + this.sessionID;
	}
	
    @Override 
    public void expire(long inactiveInterval)
    {
    	// Implement here custom expiration e.g.
    	//ShoppingCartIf.super.expire(inactiveInterval);
    	//TrackVisitsIf.super.expire(inactiveInterval);
    }
    
    public void expireAll(long inactiveInterval)
    {
    	Pipeline pipeline = getRedisConnection().pipelined();
    	pipeline.expire(getSessionKey(), inactiveInterval);
    	pipeline.expire(getHistoryKey(), inactiveInterval);
    	pipeline.expire(getLocalKey(), inactiveInterval);
    	pipeline.close();
    }
}



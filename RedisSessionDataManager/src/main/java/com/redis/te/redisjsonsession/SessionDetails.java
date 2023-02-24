package com.redis.te.redisjsonsession;

import org.springframework.stereotype.Component;

@Component
public class SessionDetails  {
	private long lastAccessedTime;
	private long creationTime;
	
	public SessionDetails() {
		this.lastAccessedTime = System.currentTimeMillis() / 1000L;
		this.creationTime = System.currentTimeMillis() / 1000L;
	}
	
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
}

# Redis as a Session Store for Java

This repository proposes a Java proof-of-concept of a Redis session store using the JSON format for the session data.

* Interfaces extend the basic JSON session object to store the desired data structures and manage multi-object session data
* TTL can be extended atomically for all the session data structures
* The JedisUnified client library (supported by Redis) is adopted
* Objects are serialized using the Jackson ObjectMapper

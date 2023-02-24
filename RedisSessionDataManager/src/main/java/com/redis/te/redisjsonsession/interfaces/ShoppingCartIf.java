package com.redis.te.redisjsonsession.interfaces;

import java.util.ArrayList;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;

public interface ShoppingCartIf {
	JedisPooled getDbConnection();
	String getSessionKey();

	default public void shoppingCartInit() {
		getDbConnection().jsonSet(getSessionKey(), new Path("cart"), new ArrayList<>());
	}

	default public void shoppingCartItemAppend(ShoppingCartItem item) {
		// Replace the item if the itemId exists
		shoppingCartItemRemove(item.getItemId());
		getDbConnection().jsonArrAppend(getSessionKey(), new Path("cart"), item);
	}
	
	default public void shoppingCartItemRemove(String itemId) {
		getDbConnection().jsonDel(getSessionKey(), 
				new Path("$.cart[?(@.itemId==\""+itemId+"\")]"));
	}

	default public ShoppingCartItem shoppingCartItemGet(String itemId) throws JsonMappingException, JsonProcessingException {
		// Path query can return multiple entries. Cart items are itemId-unique (pk)
		// Let's get the first one either way
		JSONArray json = new JSONArray(getDbConnection().jsonGetAsPlainString(getSessionKey(), 
									new Path("$.cart[?(@.itemId==\""+itemId+"\")]")));
		
		if (json.isEmpty()) {
			return new ShoppingCartItem();
		}
		
		// Now deserializing the json cart item to ShoppingCartItem
		ObjectMapper objectMapper = new ObjectMapper();
		ShoppingCartItem item = objectMapper.readValue(json.getJSONObject(0).toString(), ShoppingCartItem.class);
		return item;
	}

	
	default public ArrayList<ShoppingCartItem> shoppingCartItemGetAll() throws JsonMappingException, JsonProcessingException {
		String cart = getDbConnection().jsonGetAsPlainString(getSessionKey(), new Path("cart"));
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayList<ShoppingCartItem> tmp = objectMapper.readValue(cart, new TypeReference<ArrayList<ShoppingCartItem>>(){});	
		
		ArrayList<ShoppingCartItem> items = new ArrayList<>();
		for (ShoppingCartItem item : tmp) {
			items.add(item);
		}
		return items;
	}
	
	
	default void expire(long inactiveInterval) {
		getDbConnection().expire(getSessionKey(), inactiveInterval);
	}
}


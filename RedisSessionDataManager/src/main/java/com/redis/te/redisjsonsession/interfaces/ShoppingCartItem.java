package com.redis.te.redisjsonsession.interfaces;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoppingCartItem {
	public String itemId;
	public BigDecimal itemCost;
	public int quantity;
	
	public ShoppingCartItem() {
		
	}
	
	public ShoppingCartItem(String itemId, BigDecimal itemCost, int quantity) {
		super();
		this.itemId = itemId;
		this.itemCost = itemCost;
		this.quantity = quantity;
	}
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getItemCost() {
		return itemCost;
	}

	public void setItemCost(BigDecimal itemCost) {
		this.itemCost = itemCost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int incrQuantity(int quantity) {
		this.quantity = this.quantity + quantity;
		return this.quantity;
	}

	public int decrQuantity(int quantity) {
		this.quantity = this.quantity - quantity;
		return this.quantity;
	}
	
	public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String cartJson = objectMapper.writeValueAsString(this);
		return cartJson;
	}
}

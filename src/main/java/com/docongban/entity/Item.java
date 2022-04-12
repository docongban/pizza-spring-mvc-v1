package com.docongban.entity;

public class Item extends Product {

	private int quantity;
	private long prices;
	
	public Item() {
		
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getPrices() {
		return prices;
	}

	public void setPrices(long prices) {
		this.prices = prices;
	}
	
	
}

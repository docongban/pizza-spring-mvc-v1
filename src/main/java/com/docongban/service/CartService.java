package com.docongban.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.docongban.entity.Item;

@Service
public interface CartService {

	List<Item> getItemProduct(ArrayList<Item> itemList);
	
	//get total cart price
	long getTotalCart(ArrayList<Item> itemList);
}

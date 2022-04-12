package com.docongban.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docongban.entity.Item;
import com.docongban.entity.Product;
import com.docongban.repository.ProductRepository;
import com.docongban.service.CartService;

@Component
public class CartServiceImpl implements CartService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Item> getItemProduct(ArrayList<Item> itemList){
		List<Item> items = new ArrayList<>();
		Product product = new Product();
		
		if(itemList.size()>0) {
			for(Item i: itemList) {
				product=productRepository.findProductById(i.getId());
					
				Item item=new Item();
				
				item.setId(product.getId());
				item.setTitle(product.getTitle());
				item.setPrice(product.getPrice());
				item.setThumbnail(product.getThumbnail());
				item.setContent(product.getContent());
				item.setQuantity(i.getQuantity());
				item.setPrices(i.getQuantity()*product.getPrice()); 
				
				items.add(item);
			}
		}
		
		return items;
	}
	
	public long getTotalCart(ArrayList<Item> itemList) {
		long sum=0;
		Product product = new Product();
		
		if(itemList.size()>0) {
			for(Item i: itemList) {
				product = productRepository.findProductById(i.getId());
				
				sum+=product.getPrice()*i.getQuantity();
			}
		}
		
		return sum;
	}
}

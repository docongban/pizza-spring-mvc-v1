package com.docongban.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docongban.entity.Product;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	public List<Product> getAllProductByCategoryId(int id){
		return productRepository.findAllByCategoryId(id);
	}
	
	public List<Product> getAllProductBySearchName(String keyword){
		return productRepository.searchByName(keyword);
	}
}

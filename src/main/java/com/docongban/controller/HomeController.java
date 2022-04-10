package com.docongban.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.docongban.entity.Category;
import com.docongban.entity.Product;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;

@Controller
public class HomeController {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/")
	public String home(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		//get all product
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		
		return "index";
	}
	
	@GetMapping("/category/{id}")
	public String productByCate(Model model, @PathVariable int id) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		//get all product by category id
		model.addAttribute("products", productService.getAllProductByCategoryId(id));
		
		//get category by id (to show name int html)
		model.addAttribute("category", categoryRepository.getById(id));
		
		return "category";
	}
	
	//search
	@GetMapping("/search")
	public String search(Model model,@RequestParam("keyword") String keyword) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("products", productService.getAllProductBySearchName(keyword));
		
		return "search";
	}
	
}

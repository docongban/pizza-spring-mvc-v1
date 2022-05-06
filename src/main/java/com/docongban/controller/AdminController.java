package com.docongban.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docongban.entity.Category;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.entity.Product;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.repository.ProductRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	//bill management
	@GetMapping("/bill")
	public String getBill(Model model) {
		
		DecimalFormat df = new DecimalFormat(",###");
		
		List<OrderAccount> orderAccounts = orderAccountRepository.findAll();
		List<OrderDetail> orderDetails = orderDetailRepository.findAll();
		List<String> totalPrice = new ArrayList<>();
		
		for(OrderAccount oc: orderAccounts) {
			long total=0;
			for(OrderDetail od: orderDetails) {
				if(oc.getId()==od.getOrderAccountId()) {
					total+=od.getProductPrice();
				}
			}
			totalPrice.add(df.format(total));
		}
		
		model.addAttribute("orderAccounts", orderAccounts);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("totalPrice", totalPrice);
		
		return "admin/bill";
	}
	
	//handle bill complete button 
	@Transactional
	@GetMapping("/bill/complete/{id}")
	public String checkComplete(@PathVariable int id) {
		
		orderAccountRepository.updateStatus(id);

		return "redirect:/admin/bill";
	}
	
	//category management
	//get all catagory
	@GetMapping("/categories")
	public String getAllCategory(Model model) {
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		
		return "admin/categories";
	}
	
	//add category
	@GetMapping("/category/create")
	public String addCategory() {
		return "/admin/formCategory";
	}
	//save
//	@PostMapping("/categories/save")
//	public String handleAddCategory(@ModelAttribute Category category, @RequestParam("nameCategory") String nameCategory) {
//		
//		//get datetime now 
//		Date d=new Date();
//		category.setName(nameCategory);
//		category.setCreatedAt(new java.sql.Timestamp(d.getTime()));
//		category.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
//		categoryRepository.save(category);
//		
//		return "redirect:/admin/categories";
//	}
	@PostMapping("/categories/save")
	public String handleAddCategory(@ModelAttribute("category") Category category) {
		
		//get datetime now 
		Date d=new Date();
		category.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		category.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		categoryRepository.save(category);
		
		return "redirect:/admin/categories";
	}
	//edit save
	@PostMapping("/categories/edit")
	public String handleEditCategory(@ModelAttribute("category") Category category) {
		
		//get datetime now 
		Date d=new Date();
		category.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		category.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		categoryRepository.save(category);
		
		return "redirect:/admin/categories";
	}
	//get product id 
	@GetMapping("/category/{id}/edit")
	public String editCategory(@PathVariable int id, Model model) {
		
		model.addAttribute("category", categoryRepository.findById(id).get());
		return "/admin/formCategory";
	}
	//delete
	@GetMapping("/category/{id}/delete")
	public String deleteCategory(@PathVariable int id, Model model) {
		
		Category category = categoryRepository.findById(id).get();
		categoryRepository.delete(category);
		
		return "redirect:/admin/categories";
	}
	
	//product management
	//add product
	@GetMapping("/products")
	public String getAllProduct(Model model) {
		
		List<Product> products = productRepository.findAll();
		model.addAttribute("products", products);
		
		return "admin/products";
	}
	
	@GetMapping("/products/create")
	public String addProduct(Model model) {
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		
		return "/admin/formProduct";
	}
	//save
//	@PostMapping("/products/save")
//	public String handleAddProduct(@ModelAttribute Product product, @RequestParam("nameProduct") String nameProduct,
//			@RequestParam("contentProduct") String contentProduct, @RequestParam("priceProduct") long priceProduct,
//			@RequestParam("thumbnailProduct") String thumbnailProduct) {
//		
//		
//		product.setTitle(thumbnailProduct);
//		product.setContent(contentProduct);
//		product.setPrice(priceProduct);
//		product.setThumbnail(thumbnailProduct);
//		//get datetime now 
//		Date d=new Date();
//		product.setCreatedAt(new java.sql.Timestamp(d.getTime()));
//		product.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
//		productRepository.save(product);
//		
//		return "redirect:/admin/products";
//	}
	@PostMapping("/products/save")
	public String handleAddProduct(@ModelAttribute("product") Product product) {
		
		//get datetime now 
		Date d=new Date();
		product.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		product.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		productRepository.save(product);
		
		return "redirect:/admin/products";
	}
	//edit save
	@PostMapping("/product/edit")
	public String handleEditProduct(@ModelAttribute("product") Product product) {
		
		//get datetime now 
		Date d=new Date();
		product.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		product.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		productRepository.save(product);
		
		return "redirect:/admin/products";
	}
	//get product id 
	@GetMapping("/product/{id}/edit")
	public String editProduct(@PathVariable int id, Model model) {
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		
		model.addAttribute("product", productRepository.findById(id).get());
		return "/admin/formProduct";
	}
	//delete
	@GetMapping("/product/{id}/delete")
	public String deleteProduct(@PathVariable int id, Model model) {
		
		Product product = productRepository.findById(id).get();
		productRepository.delete(product);
		
		return "redirect:/admin/products";
	}
}

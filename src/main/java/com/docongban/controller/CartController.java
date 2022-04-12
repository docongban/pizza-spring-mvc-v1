package com.docongban.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.entity.Item;
import com.docongban.entity.Order;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.CartService;

@Controller
public class CartController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderRepository orderRepository;
	
	//add to cart
	@GetMapping("/addToCart/{id}")
	public String getProductByIdToCart(Model model, @PathVariable int id, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		ArrayList<Item> itemList = new ArrayList<>();
		Item item=new Item();
		item.setId(id);
		item.setQuantity(1);
		
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		if(item_list==null) {
			itemList.add(item);
			session.setAttribute("item-list", itemList);
            session.setMaxInactiveInterval(60*60*24);
            return "redirect:/cart";
		}else {
			itemList = item_list;
			boolean exist = false;
            for (Item i : item_list) {
                if (i.getId() == id) {
                	
                    exist = true;
                    int quantity = i.getQuantity();
                    quantity++;
                    i.setQuantity(quantity);
                    return "redirect:/cart";
                }
            }
            if (!exist) {
            	itemList.add(item);
            	return "redirect:/cart";
            }
		}
		
		return "cart";
	}
	
	@GetMapping("/cart")
	public String cartProduct(Model model, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		List<Item> items = null;
		int cartSize=0;
		if(item_list!=null) {
			
			items=cartService.getItemProduct(item_list);
			long totalPrice = cartService.getTotalCart(item_list);
			
			model.addAttribute("items", items);
			model.addAttribute("totalPrice", totalPrice);
			
			cartSize = items.size();
			session.setAttribute("itemsSession", items);
		}else {
			cartSize = 0;
		}
		session.setAttribute("cartSize", cartSize);
		return "cart";
	}
	
	@GetMapping("/quantity-dec/{id}")
	public String quantityDecCart(Model model,  @PathVariable int id, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		
		for(Item i: item_list) {
			if(i.getId()==id && i.getQuantity()>1) {
				int quantity = i.getQuantity();
				quantity--;
				i.setQuantity(quantity);
				break;
			}
		}
		
		return "redirect:/cart";
	}
	
	@GetMapping("/quantity-inc/{id}")
	public String quantityIncCart(Model model,  @PathVariable int id, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		
		for(Item i: item_list) {
			if(i.getId()==id) {
				int quantity = i.getQuantity();
				quantity++;
				i.setQuantity(quantity);
				break;
			}
		}
		
		return "redirect:/cart";
	}
	
	@GetMapping("/removeProductCart/{id}")
	public String removeProductCart(Model model,  @PathVariable int id, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		
		if(item_list!=null) {
			for(Item i: item_list) {
				if(i.getId()==id) {
					item_list.remove(item_list.indexOf(i));
					break;
				}
			}
		}
		return "redirect:/cart";
	}
	
	@GetMapping("/check-out")
	public String checkOut(Model model, HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		ArrayList<Item> item_list = (ArrayList<Item>) session.getAttribute("item-list");
		Account accountSession = (Account) session.getAttribute("account");
		
		Date d=new Date();

		if(item_list!=null && accountSession!=null) {
			for(Item i: item_list) {
				Order order=new Order();
				order.setProductId(i.getId());
				order.setAccountId(accountSession.getId());
				order.setQuantiy(i.getQuantity());
				order.setOrderDate(new java.sql.Timestamp(d.getTime()));
				
				orderRepository.save(order);
			}
			item_list.clear();
			return "complete";
		}else {
			if(accountSession==null) {
				return "login";
			}else {
				return "cart";
			}
		}
	}
}

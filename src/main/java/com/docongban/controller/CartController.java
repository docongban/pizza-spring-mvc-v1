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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.entity.Item;
import com.docongban.entity.Order;
import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;
import com.docongban.repository.OrderRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	//add to cart
	@GetMapping("/addToCart/{id}")
	public RedirectView getProductByIdToCart(Model model, @PathVariable int id, HttpSession session) {
		
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
			List<Item> items = cartService.getItemProduct(itemList);
			session.setAttribute("itemsSession", items);
			session.setAttribute("item-list", itemList);
			session.setAttribute("cartSize", itemList.size());
            session.setMaxInactiveInterval(60*60*24);
            return new RedirectView("/");
		}else {
			boolean exist = false;
            for (Item i : item_list) {
                if (i.getId() == id) {
                	
                    exist = true;
                    int quantity = i.getQuantity();
                    quantity++;
                    i.setQuantity(quantity);
                    break;
                }
            }
            if (!exist) {
            	item_list.add(item);
            }
		}
		
		List<Item> items = cartService.getItemProduct(item_list);
		session.setAttribute("itemsSession", items);
		
		session.setAttribute("item-list", item_list);
		session.setAttribute("cartSize", item_list.size());
		
		return new RedirectView("/");
	}
	
	@GetMapping()
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
		List<Item> items = null;
		Account accountSession = (Account) session.getAttribute("account");
		
		Date d=new Date();
		if(item_list!=null && accountSession!=null) {
			
			items = cartService.getItemProduct(item_list);
			
			OrderAccount orderAccount = new OrderAccount();
			orderAccount.setAccountFullname(accountSession.getFullname());
			orderAccount.setAccountPhone(accountSession.getPhone());
			orderAccount.setAccountEmail(accountSession.getEmail());
			orderAccount.setAccountAddress(accountSession.getAddress());
			orderAccount.setOrderDate(new java.sql.Timestamp(d.getTime()));
			orderAccountRepository.save(orderAccount);
			
			for(Item i: items) {
				
				Order order=new Order();
				
				order.setProductId(i.getId());
				order.setAccountId(accountSession.getId());
				order.setOrderAccountId(orderAccountRepository.getMaxId());
				order.setQuantiy(i.getQuantity());
				order.setOrderDate(new java.sql.Timestamp(d.getTime()));
				
				orderRepository.save(order);
				
				
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setProductTitle(i.getTitle());
				orderDetail.setProductThumbnail(i.getThumbnail());
				orderDetail.setProductContent(i.getContent());
				orderDetail.setProductPrice(i.getPrices());
				orderDetail.setOrderQuantity(i.getQuantity());
				orderDetail.setOrderAccountId(orderAccountRepository.getMaxId());
				orderDetailRepository.save(orderDetail);
			}
			item_list.clear();
			session.setAttribute("cartSize", 0);
			session.removeAttribute("item-list");
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

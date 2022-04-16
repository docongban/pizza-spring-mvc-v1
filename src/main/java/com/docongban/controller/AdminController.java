package com.docongban.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docongban.entity.OrderAccount;
import com.docongban.entity.OrderDetail;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.OrderAccountRepository;
import com.docongban.repository.OrderDetailRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	OrderAccountRepository orderAccountRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
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
	
	@GetMapping("/bill/complete/{id}")
	public String checkComplete(Model model,  @PathVariable int id) {
		
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
		
		orderAccountRepository.updateStatus(id);
		
		model.addAttribute("orderAccounts", orderAccounts);
		model.addAttribute("orderDetails", orderDetails);
		model.addAttribute("totalPrice", totalPrice);

		
		
		return "redirect:/admin/bill";
	}
}

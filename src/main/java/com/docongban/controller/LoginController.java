package com.docongban.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.service.AccountService;

@Controller
public class LoginController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired(required = true)
	AccountService accountService;
	
	@GetMapping("/register")
	public String register(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		return "register";
	}
	
	@PostMapping("/register")
	public String createAccount(Model model,@ModelAttribute Account account, @RequestParam("fullname") String fullname, 
			@RequestParam("email") String email, @RequestParam("address") String address, @RequestParam("phone") String phone) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		boolean accountCheck = accountService.checkAccountExisted(phone);
		
		if(!accountCheck ) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("email", email);
			model.addAttribute("address", address);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneExisted", "Số điện thoại đã được đăng kí vui lòng thử với số khác");
			
			return "register";
		}
		
		//get datetime now 
		Date d=new Date();
		account.setCreatedAt(new java.sql.Timestamp(d.getTime()));
		account.setUpdatedAt(new java.sql.Timestamp(d.getTime()));
		
		accountRepository.save(account);
		
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		return "login";
	}
	
	@PostMapping("/login")
	public String checkLogin(Model model, 
							@RequestParam("phone") String phone, @RequestParam("password") String password,
							HttpSession session) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		Account account = accountService.accountLogin(phone, password);
		if(account != null) {
			session.setAttribute("account", account);
			
			session.setAttribute("fullname", account.getFullname());
			session.setAttribute("role", account.getRole());
			session.setMaxInactiveInterval(60*60*24);
			return "redirect:/";
		}else {
			model.addAttribute("phone", phone);
			model.addAttribute("error", "Vui lòng kiểm tra lại số điện thoại hoặc mật khẩu");
		}
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.removeAttribute("fullname");
		
		return "redirect:/login";
	}
}

package com.docongban.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.docongban.entity.Account;
import com.docongban.entity.Category;
import com.docongban.jwt.JwtTokenProvider;
import com.docongban.payload.RegisterRequest;
import com.docongban.repository.AccountRepository;
import com.docongban.repository.CategoryRepository;
import com.docongban.service.AccountService;
import com.docongban.service.AuthService;

@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired(required = true)
	AccountService accountService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/register")
	public String register(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		return "register";
	}
	
	@PostMapping("/register")
	public String createAccount(
			Model model, 
			@RequestParam("fullname") String fullname,
			@RequestParam("email") String email, 
			@RequestParam("address") String address, 
			@RequestParam("phone") String phone,
			@RequestParam("password") String password
		) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		boolean accountCheck = accountService.checkAccountExisted(phone);
		
		if( !accountCheck ) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("email", email);
			model.addAttribute("address", address);
			model.addAttribute("phone", phone);
			model.addAttribute("phoneExisted", "Số điện thoại đã được đăng kí vui lòng thử với số khác");
			
			return "register";
		}
		
		// Xử lý service liên quan đến đăng ký.
		RegisterRequest registerRequest = new RegisterRequest(phone, password, fullname, email, address, password);
		authService.handleRegister(registerRequest);
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		return "login";
	}
	
	@PostMapping("/login")
	public String checkLogin(
			Model model, 
			@RequestParam("phone") String phone, 
			@RequestParam("password") String password,
			HttpServletRequest request
		) {
		
		HttpSession session = request.getSession();
		
		//get all category
		List<Category> categoris = categoryRepository.findAll();
		model.addAttribute("categoris", categoris);
		
		// Xử lý service liên quan đến đăng nhập.
		String token = authService.handleLogin(phone, password, request);
		
		// Nếu đăng nhập thành công -> lấy ra Account từ jwt:
		if(token != null) {
			Integer accountId = jwtTokenProvider.getAccountIdFromJWT(token);
			Account account = accountRepository.getById(accountId);
			session.setAttribute("account", account);
			session.setAttribute("fullname", account.getFullname());
			session.setMaxInactiveInterval(60*60*24);
			return "redirect:/";
		} else {
			model.addAttribute("phone", phone);
			model.addAttribute("error", "Vui lòng kiểm tra lại số điện thoại hoặc mật khẩu");
		}
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		authService.handleLogout(request);
		return "redirect:/auth/login";
	}
}

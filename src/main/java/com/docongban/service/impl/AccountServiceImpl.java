package com.docongban.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.docongban.custom.CustomUserDetails;
import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.service.AccountService;

@Component
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;
	
	// Check số điện thoại đã đăng ký hay chưa?
	public boolean checkAccountExisted(String phone) {
		if(accountRepository.findAccountByPhone(phone).isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	// Kiểm tra xe user có tồn tại trong Database hay không?
	@Override
	public UserDetails loadUserByUsername(String phone) {
		Account account = accountRepository.findAccountByPhone(phone).orElseThrow(
				() -> new UsernameNotFoundException("So dien thoai khong chinh xac")
		);
		return new CustomUserDetails(account);
	}
	
	@Transactional
	public UserDetails loadUserById(Integer id) {
		Account account = accountRepository.findById(id).orElseThrow(
				() -> new UsernameNotFoundException("Account not found with id: " + id)
		);
		
		return new CustomUserDetails(account);
	}

}

package com.docongban.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docongban.entity.Account;
import com.docongban.repository.AccountRepository;
import com.docongban.service.AccountService;

@Component
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;
	
	public Account accountLogin(String phone, String password) {
		
		Account account = accountRepository.findAccount(phone, password);
		
		if(account!=null) {
			return account;
		}
		return null;
	}
	
	public boolean checkAccountExisted(String phone) {
		
		Account account = accountRepository.findAccountByPhone(phone);
		
		if(account==null) {
			return true;
		}
		
		return false;
	}
}

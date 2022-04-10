package com.docongban.service;

import org.springframework.stereotype.Service;

import com.docongban.entity.Account;

@Service
public interface AccountService {

	Account accountLogin(String phone, String password);
	
	boolean checkAccountExisted(String phone);
}

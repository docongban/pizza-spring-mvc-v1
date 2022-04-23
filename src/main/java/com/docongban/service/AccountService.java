package com.docongban.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.docongban.entity.Account;

@Service
public interface AccountService extends UserDetailsService {
	
	boolean checkAccountExisted(String phone);
}

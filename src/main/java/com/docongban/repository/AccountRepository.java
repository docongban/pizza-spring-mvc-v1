package com.docongban.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.docongban.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	@Query(value = "select * from account where phone =?1 and password =?2", nativeQuery = true)
	public Account findAccount(String phone, String password);
	
	@Query(value = "select * from account where phone =?1", nativeQuery = true)
	public Optional<Account> findAccountByPhone(String phone);
	

}

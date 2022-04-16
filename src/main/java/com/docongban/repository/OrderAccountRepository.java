package com.docongban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.docongban.entity.OrderAccount;

@Repository
public interface OrderAccountRepository extends JpaRepository<OrderAccount, Integer> {

	@Query(value = "SELECT MAX(Id) FROM order_account", nativeQuery = true)
	public int getMaxId();
	
	@Query(value = "UPDATE order_account SET order_status = 1 WHERE id = ?1 ", nativeQuery = true)
	public void updateStatus(int id);
}

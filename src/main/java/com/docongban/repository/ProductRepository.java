package com.docongban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.docongban.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findAllByCategoryId(int id);
	
	//search
	@Query(value = "select * from product where title like %?1% ", nativeQuery = true)
	public List<Product> searchByName(String keyword);
	
	@Query(value = "select * from product where id= ?1 ", nativeQuery = true)
	public Product findProductById(int id);
}

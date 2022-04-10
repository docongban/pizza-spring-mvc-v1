package com.docongban.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docongban.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}

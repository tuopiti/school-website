package com.piti.java.schoolwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piti.java.schoolwebsite.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	boolean existsByName(String name);
}

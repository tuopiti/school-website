package com.piti.java.schoolwebsite.service;

import java.util.List;

import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.model.Category;

public interface CategoryService {
	CategoryDTO save(CategoryDTO categoryDTO);
	Category getById(Long id);
	CategoryDTO update(Long id, CategoryDTO categoryDTO);
	List<CategoryDTO> getCategorys(); 
}

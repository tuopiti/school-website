package com.piti.java.schoolwebsite.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CategoryMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.repository.CategoryRepository;
import com.piti.java.schoolwebsite.service.CategoryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
	private CategoryRepository categoryRepository;
	private CategoryMapper categoryMapper;
	
	@Override
	public CategoryDTO save(CategoryDTO categoryDTO) {
		Category category = categoryMapper.toEntity(categoryDTO);
		if(categoryRepository.existsByName(category.getName())) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Category is already exists");
		}
	    category = categoryRepository.save(category);
		return categoryMapper.toDTO(category);
	}

	
	@Override
	public Category getById(Long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id, HttpStatus.NOT_FOUND));
	}

	@Override
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		/*
		Category existingCategory = categoryRepository.findById(id)
	               .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id, HttpStatus.NOT_FOUND));
	    */
		Category existingCategory = getById(id);
        if (categoryRepository.existsByName(categoryDTO.getName()) && !existingCategory.getName().equals(categoryDTO.getName())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Category with this name already exists");
        }
        
        //existingCategory.setName(categoryDTO.getName());
        categoryMapper.updateEntityFromDTO(categoryDTO, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDTO(updatedCategory);
	}

	@Override
	public List<CategoryDTO> getCategorys() {
		List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
	}
	
	

}

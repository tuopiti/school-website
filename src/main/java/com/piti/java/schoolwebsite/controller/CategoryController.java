package com.piti.java.schoolwebsite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.mapper.CategoryMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {
	private CategoryService categoryService;
	private CategoryMapper categoryMapper;
	
	@PostMapping
	public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO categoryDTO){
		CategoryDTO dto = categoryService.save(categoryDTO);
		return new ResponseEntity<CategoryDTO>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getById(@PathVariable("id") long id){
		Category category = categoryService.getById(id);
		CategoryDTO dto = categoryMapper.toDTO(category);
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable("id") long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO updatedDto = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updatedDto);
    }
	
	@GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categories = categoryService.getCategorys();
        return ResponseEntity.ok(categories);
    }
}

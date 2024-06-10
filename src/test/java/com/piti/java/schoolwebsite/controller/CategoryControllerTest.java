package com.piti.java.schoolwebsite.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CategoryMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.service.CategoryService;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    private CategoryDTO categoryDTO;
    private Category category;
    private CategoryDTO updatedCategoryDTO;
    
    private CategoryDTO categoryDTO1;
    private CategoryDTO categoryDTO2;
    private List<CategoryDTO> categoryDTOList;
    
    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO(1L, "Programming");
        category = new Category(1L, "Programming");
        updatedCategoryDTO = new CategoryDTO(1L, "Java Programming");
        
        categoryDTO1 = new CategoryDTO(1L, "Programming");
        categoryDTO2 = new CategoryDTO(2L, "Design");
        categoryDTOList = Arrays.asList(categoryDTO1, categoryDTO2);
    }
    
    @Test
    void testCreateCategorySuccessfully() throws Exception {
        when(categoryService.save(any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(post("/api/v1/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDTO.getName())));
    }
    
    @Test
    void testCreateCategoryWithInvalidData() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO(1L, ""); // Invalid name

        mockMvc.perform(post("/api/v1/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetByIdSuccessfully() throws Exception {
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDTO.getName())));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(categoryService.getById(anyLong())).thenThrow(new ResourceNotFoundException("Category", "id", 1L, HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateCategorySuccessfully() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDTO.class))).thenReturn(updatedCategoryDTO);

        mockMvc.perform(put("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedCategoryDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedCategoryDTO.getName())));
    }

    @Test
    void testUpdateCategoryWithInvalidData() throws Exception {
        CategoryDTO invalidCategoryDTO = new CategoryDTO(1L, ""); // Invalid name

        mockMvc.perform(put("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidCategoryDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testUpdateCategoryNotFound() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDTO.class))).thenThrow(new ResourceNotFoundException("Category", "id", 1L, HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCategoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCategoryThrowsApiExceptionForDuplicateName() throws Exception {
        when(categoryService.update(anyLong(), any(CategoryDTO.class))).thenThrow(new ApiException(HttpStatus.BAD_REQUEST, "Category with this name already exists"));

        mockMvc.perform(put("/api/v1/category/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCategoryDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetAllCategoriesSuccessfully() throws Exception {
        when(categoryService.getCategorys()).thenReturn(categoryDTOList);

        mockMvc.perform(get("/api/v1/category")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(categoryDTO1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(categoryDTO1.getName())))
                .andExpect(jsonPath("$[1].id", is(categoryDTO2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(categoryDTO2.getName())));
    }
}

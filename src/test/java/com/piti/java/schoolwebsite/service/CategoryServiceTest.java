package com.piti.java.schoolwebsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CategoryMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.repository.CategoryRepository;
import com.piti.java.schoolwebsite.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	 
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	@Mock
    private CategoryMapper categoryMapper;
	 
	private CategoryDTO categoryDTO;
	private Category category;
	private Category updatedCategory;
	
	private CategoryDTO categoryDTO1;
    private CategoryDTO categoryDTO2;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO(1L, "Programming");
        category = new Category(1L, "Programming");
        updatedCategory = new Category(1L, "Java Programming");
        
        categoryDTO1 = new CategoryDTO(1L, "Programming");
        categoryDTO2 = new CategoryDTO(2L, "Frameworks");
        category1 = new Category(1L, "Programming");
        category2 = new Category(2L, "Frameworks");
    }
    
    @Test
    void testSaveCategorySuccessfully() {
        when(categoryRepository.existsByName(any(String.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(category);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO savedCategoryDTO = categoryService.save(categoryDTO);

        assertNotNull(savedCategoryDTO);
        assertEquals(categoryDTO.getId(), savedCategoryDTO.getId());
        assertEquals(categoryDTO.getName(), savedCategoryDTO.getName());

        verify(categoryRepository, times(1)).existsByName(categoryDTO.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toEntity(any(CategoryDTO.class));
        verify(categoryMapper, times(1)).toDTO(any(Category.class));
    }
    
    @Test
    void testSaveCategoryThrowsApiException() {
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);
        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(category);

        ApiException exception = assertThrows(ApiException.class, () -> {
            categoryService.save(categoryDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Category is already exists", exception.getMessage());

        verify(categoryRepository, times(1)).existsByName(categoryDTO.getName());
        verify(categoryRepository, times(0)).save(any(Category.class));
        verify(categoryMapper, times(1)).toEntity(any(CategoryDTO.class));
        verify(categoryMapper, times(0)).toDTO(any(Category.class));
    }

    @Test
    void testGetByIdSuccessfully() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getById(1L);

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());

        verify(categoryRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetByIdThrowsResourceNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getById(1L);
        });

        assertEquals("Category not found with id : '1'", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());

        verify(categoryRepository, times(1)).findById(1L);
    }
    
    @Test
    void testUpdateCategorySuccessfully() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any(String.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(new CategoryDTO(1L, "Java Programming"));

        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L, "Java Programming");
        doAnswer(invocation -> {
            CategoryDTO dto = invocation.getArgument(0);
            Category entity = invocation.getArgument(1);
            entity.setName(dto.getName());
            return null;
        }).when(categoryMapper).updateEntityFromDTO(any(CategoryDTO.class), any(Category.class));

        CategoryDTO result = categoryService.update(1L, updatedCategoryDTO);

        assertNotNull(result);
        assertEquals(updatedCategoryDTO.getId(), result.getId());
        assertEquals(updatedCategoryDTO.getName(), result.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).existsByName(updatedCategoryDTO.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).updateEntityFromDTO(any(CategoryDTO.class), any(Category.class));
        verify(categoryMapper, times(1)).toDTO(any(Category.class));
    }

    
    @Test
    void testUpdateCategoryThrowsApiExceptionForDuplicateName() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);

        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L, "Java Programming");

        ApiException exception = assertThrows(ApiException.class, () -> {
            categoryService.update(1L, updatedCategoryDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Category with this name already exists", exception.getMessage());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).existsByName(updatedCategoryDTO.getName());
        verify(categoryRepository, times(0)).save(any(Category.class));
        verify(categoryMapper, times(0)).toDTO(any(Category.class));
    }
    
    @Test
    void testUpdateCategoryNoExceptionForSameName() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any(String.class))).thenReturn(true);

        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L, "Programming");

        // Mock the behavior of the mapper to ensure proper entity update
        doNothing().when(categoryMapper).updateEntityFromDTO(any(CategoryDTO.class), any(Category.class));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(any(Category.class))).thenReturn(updatedCategoryDTO);

        CategoryDTO result = categoryService.update(1L, updatedCategoryDTO);

        assertNotNull(result);
        assertEquals(updatedCategoryDTO.getId(), result.getId());
        assertEquals(updatedCategoryDTO.getName(), result.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).existsByName(updatedCategoryDTO.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).updateEntityFromDTO(any(CategoryDTO.class), any(Category.class));
    }
    
    @Test
    void testGetCategorys() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        when(categoryMapper.toDTO(category1)).thenReturn(categoryDTO1);
        when(categoryMapper.toDTO(category2)).thenReturn(categoryDTO2);

        List<CategoryDTO> categoryDTOList = categoryService.getCategorys();

        assertNotNull(categoryDTOList);
        assertEquals(2, categoryDTOList.size());
        assertEquals(categoryDTO1.getId(), categoryDTOList.get(0).getId());
        assertEquals(categoryDTO1.getName(), categoryDTOList.get(0).getName());
        assertEquals(categoryDTO2.getId(), categoryDTOList.get(1).getId());
        assertEquals(categoryDTO2.getName(), categoryDTOList.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toDTO(category1);
        verify(categoryMapper, times(1)).toDTO(category2);
    }
    
}

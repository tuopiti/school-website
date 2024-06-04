package com.piti.java.schoolwebsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.piti.java.schoolwebsite.dto.CategoryDTO;
import com.piti.java.schoolwebsite.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
	Category toEntity(CategoryDTO categoryDTO);
	CategoryDTO toDTO(Category category);
	
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);
}

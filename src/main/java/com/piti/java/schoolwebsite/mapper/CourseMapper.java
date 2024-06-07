package com.piti.java.schoolwebsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.service.CategoryService;

@Mapper(componentModel = "spring",uses = {CategoryService.class})
public interface CourseMapper {
	
	@Mapping(target = "category", source = "courseDTO.categoryId")
	Course toEntity(CourseDTO courseDTO);
	
	@Mapping(target = "categoryId", source = "category.id")
	CourseDTO toDTO(Course course);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "category", source = "courseDTO.categoryId")
	void updateCourseFromDTO(CourseDTO courseDTO, @MappingTarget Course course);
}

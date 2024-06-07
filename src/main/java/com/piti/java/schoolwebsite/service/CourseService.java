package com.piti.java.schoolwebsite.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.model.Course;

public interface CourseService {
	CourseDTO save(CourseDTO courseDTO);
	Course getById(Long id);
	CourseDTO update(Long id, CourseDTO courseDTO);
	void delete(Long id);
	Page<CourseDTO> getCourses(Map<String, String> params);
}

package com.piti.java.schoolwebsite.service;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.model.Course;

public interface CourseService {
	CourseDTO save(CourseDTO courseDTO);
	Course getById(Long id);
}

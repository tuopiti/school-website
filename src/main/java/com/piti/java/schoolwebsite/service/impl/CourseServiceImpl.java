package com.piti.java.schoolwebsite.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.repository.CourseRepository;
import com.piti.java.schoolwebsite.service.CourseService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService{
	private CourseRepository courseRepository;
	private CourseMapper courseMapper;

	@Override
	public CourseDTO save(CourseDTO courseDTO) {
		Course course = courseMapper.toEntity(courseDTO);
		course = courseRepository.save(course);
		return courseMapper.toDTO(course);
	}

	@Override
	public Course getById(Long id) {
		return courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course", "id", id, HttpStatus.NOT_FOUND));
	}

}

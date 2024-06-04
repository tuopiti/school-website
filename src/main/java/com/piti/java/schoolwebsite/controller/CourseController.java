package com.piti.java.schoolwebsite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.service.CourseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/course")
@AllArgsConstructor
public class CourseController {
	private CourseService courseService;
	private CourseMapper courseMapper;
	
	@PostMapping
	public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO courseDTO){
		CourseDTO dto = courseService.save(courseDTO);
		return new ResponseEntity<CourseDTO>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CourseDTO> getById(@PathVariable("id") Long id){
		Course course = courseService.getById(id);
		return ResponseEntity.ok(courseMapper.toDTO(course));	
	}
}

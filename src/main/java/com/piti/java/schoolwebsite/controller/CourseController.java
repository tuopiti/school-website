package com.piti.java.schoolwebsite.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.dto.PageDTO;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.mapper.PageMapper;
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
	private PageMapper pageMapper;
	
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
	
	@PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable("id") long id, @RequestBody CourseDTO courseDTO) {
		CourseDTO updatedDto = courseService.update(id, courseDTO);
        return ResponseEntity.ok(updatedDto);
    }
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
		courseService.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<?> getHotelsList(@RequestParam Map<String, String> params){
		Page<CourseDTO> courses = courseService.getCourses(params);
		//PageDTO pageDTO = new PageDTO(courses);
		PageDTO pageDTO = pageMapper.toDTO(courses);
		return ResponseEntity.ok(pageDTO);
	}
}

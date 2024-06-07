package com.piti.java.schoolwebsite.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.repository.CourseRepository;
import com.piti.java.schoolwebsite.service.CourseService;
import com.piti.java.schoolwebsite.spec.CourseFilter;
import com.piti.java.schoolwebsite.spec.CourseSpec;
import com.piti.java.schoolwebsite.utils.PageUtils;

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

	@Override
	public CourseDTO update(Long id, CourseDTO courseDTO) {
		Course existingCourse = getById(id);
		if (courseRepository.existsByName(courseDTO.getName()) && !existingCourse.getName().equals(courseDTO.getName())) {
	         throw new ApiException(HttpStatus.BAD_REQUEST, "Course with this name already exists");
	    }
		
		courseMapper.updateCourseFromDTO(courseDTO, existingCourse);
		Course updatedCourse = courseRepository.save(existingCourse);
		return courseMapper.toDTO(updatedCourse);
	}

	@Override
	public void delete(Long id) {
		Course course = getById(id);
		courseRepository.delete(course);
	}

	@Override
	public Page<CourseDTO> getCourses(Map<String, String> params) {
		Pageable pageable = PageUtils.getPageable(params);
		CourseFilter courseFilter = new CourseFilter();
		
		if(params.containsKey("courseId")) {
			courseFilter.setCourseId(MapUtils.getInteger(params, "courseId"));
		}
		
		if(params.containsKey("courseName")) {
			courseFilter.setCourseName(MapUtils.getString(params, "courseName"));
		}
		
		if(params.containsKey("categoryId")) {
			courseFilter.setCategoryId(MapUtils.getInteger(params, "categoryId"));
		}
		
		if(params.containsKey("categoryName")) {
			courseFilter.setCategoryName(MapUtils.getString(params, "categoryName"));
		}
		
		CourseSpec courseSpec = new CourseSpec(courseFilter);
		Page<Course> course = courseRepository.findAll(courseSpec, pageable);
		
		List<CourseDTO> courseDTO = course.getContent().stream()
						.map(courseMapper::toDTO)
						.collect(Collectors.toList());
		
		Page<CourseDTO> page = new PageImpl<>(courseDTO, pageable, course.getTotalElements());
		return page;
	}

}

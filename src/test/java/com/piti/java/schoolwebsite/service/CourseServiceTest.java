package com.piti.java.schoolwebsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.repository.CourseRepository;
import com.piti.java.schoolwebsite.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
	@Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseDTO courseDTO;
    private Course course;

    @BeforeEach
    void setUp() {
        Category category = new Category(1L, "Programming");
        courseDTO = new CourseDTO(1L, "Java Basics", "Introduction to Java", "http://example.com/java", "Weekly", new BigDecimal("100.00"), new BigDecimal("80.00"), 1L, true);
        course = new Course(1L, "Advanced Java", "Advanced topics in Java", "http://example.com/advanced-java", "Weekly", new BigDecimal("200.00"), new BigDecimal("180.00"), new Category(1L, "Programming"), true);
    }

    @Test
    void testSaveCourseSuccessfully() {
        when(courseMapper.toEntity(any(CourseDTO.class))).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toDTO(any(Course.class))).thenReturn(courseDTO);

        CourseDTO savedCourseDTO = courseService.save(courseDTO);

        assertNotNull(savedCourseDTO);
        assertEquals(courseDTO.getId(), savedCourseDTO.getId());
        assertEquals(courseDTO.getName(), savedCourseDTO.getName());
        assertEquals(courseDTO.getDescription(), savedCourseDTO.getDescription());
        assertEquals(courseDTO.getVideoUrl(), savedCourseDTO.getVideoUrl());
        assertEquals(courseDTO.getMeetingSchedule(), savedCourseDTO.getMeetingSchedule());
        assertEquals(courseDTO.getTuitionFeeStudent(), savedCourseDTO.getTuitionFeeStudent());
        assertEquals(courseDTO.getTuitionFeeEmployee(), savedCourseDTO.getTuitionFeeEmployee());
        assertEquals(courseDTO.getCategoryId(), savedCourseDTO.getCategoryId());
        assertEquals(courseDTO.isDiscountEligible(), savedCourseDTO.isDiscountEligible());

        verify(courseMapper, times(1)).toEntity(any(CourseDTO.class));
        verify(courseRepository, times(1)).save(any(Course.class));
        verify(courseMapper, times(1)).toDTO(any(Course.class));
    }
    
    
    @Test
    void testGetByIdSuccessfully() {
        // Mocking repository behavior
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Calling the getById method
        Course foundCourse = courseService.getById(1L);

        // Assertions
        assertNotNull(foundCourse);
        assertEquals(course.getId(), foundCourse.getId());
        assertEquals(course.getName(), foundCourse.getName());
        assertEquals(course.getDescription(), foundCourse.getDescription());
        assertEquals(course.getVideoUrl(), foundCourse.getVideoUrl());
        assertEquals(course.getMeetingSchedule(), foundCourse.getMeetingSchedule());
        assertEquals(course.getTuitionFeeStudent(), foundCourse.getTuitionFeeStudent());
        assertEquals(course.getTuitionFeeEmployee(), foundCourse.getTuitionFeeEmployee());
        assertEquals(course.getCategory(), foundCourse.getCategory());
        assertEquals(course.isDiscountEligible(), foundCourse.isDiscountEligible());
    }

    @Test
    void testGetByIdThrowsResourceNotFoundException() {
        // Mocking repository behavior to return an empty optional
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Calling the getById method and expecting an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getById(1L);
        });

        // Assertions on the thrown exception
        assertEquals("Course not found with id : '1'", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Course", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
    }
    
    @Test
    void testUpdateCourseSuccessfully() {
        // Mocking repository behavior
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByName(any(String.class))).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toDTO(any(Course.class))).thenReturn(courseDTO);

        CourseDTO updatedCourseDTO = courseService.update(1L, courseDTO);

        // Assertions
        assertNotNull(updatedCourseDTO);
        assertEquals(courseDTO.getId(), updatedCourseDTO.getId());
        assertEquals(courseDTO.getName(), updatedCourseDTO.getName());
        assertEquals(courseDTO.getDescription(), updatedCourseDTO.getDescription());
        assertEquals(courseDTO.getVideoUrl(), updatedCourseDTO.getVideoUrl());
        assertEquals(courseDTO.getMeetingSchedule(), updatedCourseDTO.getMeetingSchedule());
        assertEquals(courseDTO.getTuitionFeeStudent(), updatedCourseDTO.getTuitionFeeStudent());
        assertEquals(courseDTO.getTuitionFeeEmployee(), updatedCourseDTO.getTuitionFeeEmployee());
        assertEquals(courseDTO.getCategoryId(), updatedCourseDTO.getCategoryId());
        assertEquals(courseDTO.isDiscountEligible(), updatedCourseDTO.isDiscountEligible());

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).existsByName(courseDTO.getName());
        verify(courseRepository, times(1)).save(any(Course.class));
        verify(courseMapper, times(1)).toDTO(any(Course.class));
    }
    
    @Test
    void testUpdateCourseThrowsApiExceptionForDuplicateName() {
        // Mocking the repository behavior to return a course when getById is called
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByName(courseDTO.getName())).thenReturn(true);

        // Asserting ApiException when update is called with a duplicate name
        ApiException exception = assertThrows(ApiException.class, () -> {
            courseService.update(1L, courseDTO);
        });

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Course with this name already exists", exception.getMessage());

        // Verify interactions with the repository and mapper
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).existsByName(courseDTO.getName());
        verify(courseRepository, times(0)).save(any(Course.class));
        verify(courseMapper, times(0)).toDTO(any(Course.class));
    }

    @Test
    void testUpdateCourseThrowsResourceNotFoundExceptionForNonExistingCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.update(1L, courseDTO);
        });

        verify(courseRepository).findById(1L);
        // Ensure that other methods are not invoked
        verify(courseRepository, never()).existsByName(any());
        verify(courseMapper, never()).toEntity(any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testDeleteCourseSuccessfully() {
        // Mocking the repository behavior to return a course when getById is called
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Calling the delete method
        courseService.delete(1L);

        // Verify that the course was retrieved and then deleted
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteCourseThrowsResourceNotFoundException() {
        // Mocking repository behavior to return an empty optional
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Calling the delete method and expecting an exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            courseService.delete(1L);
        });

        // Assertions on the thrown exception
        assertEquals("Course not found with id : '1'", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Course", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());

        // Verify that the course was attempted to be retrieved but not deleted
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(0)).delete(course);
    }

    @Test
    void testGetCoursesSuccessfully() {
        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("courseName", "Java");
        params.put("categoryName", "Programming");

        // Mocking Pageable and Page
        Pageable pageable = PageRequest.of(0, 2);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        // Mocking repository and mapper behavior
        when(courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(coursePage);
        when(courseMapper.toDTO(any(Course.class))).thenReturn(courseDTO);

        // Call the method
        Page<CourseDTO> result = courseService.getCourses(params);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(courseDTO, result.getContent().get(0));

        // Verify interactions
        verify(courseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(courseMapper, times(1)).toDTO(any(Course.class));
    }

    @Test
    void testGetCoursesWithDifferentParameters() {
        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("courseId", "1");
        params.put("categoryId", "1");

        // Mocking Pageable and Page
        Pageable pageable = PageRequest.of(0, 2);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        // Mocking repository and mapper behavior
        when(courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(coursePage);
        when(courseMapper.toDTO(any(Course.class))).thenReturn(courseDTO);

        // Call the method
        Page<CourseDTO> result = courseService.getCourses(params);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(courseDTO, result.getContent().get(0));

        // Verify interactions
        verify(courseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(courseMapper, times(1)).toDTO(any(Course.class));
    }

    @Test
    void testGetCoursesWithNoResults() {
        // Prepare parameters
        Map<String, String> params = new HashMap<>();
        params.put("courseName", "NonExistentCourse");

        // Mocking Pageable and Page
        Pageable pageable = PageRequest.of(0, 2);
        Page<Course> coursePage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        // Mocking repository and mapper behavior
        when(courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(coursePage);

        // Call the method
        Page<CourseDTO> result = courseService.getCourses(params);

        // Assertions
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());

        // Verify interactions
        verify(courseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(courseMapper, times(0)).toDTO(any(Course.class));
    }
}

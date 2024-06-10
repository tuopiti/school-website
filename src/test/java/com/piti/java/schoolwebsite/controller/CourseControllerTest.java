package com.piti.java.schoolwebsite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.CourseDTO;
import com.piti.java.schoolwebsite.dto.PageDTO;
import com.piti.java.schoolwebsite.dto.PaginationDTO;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.CourseMapper;
import com.piti.java.schoolwebsite.mapper.PageMapper;
import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.service.CourseService;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    private PageMapper pageMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Course course;
    private CourseDTO courseDTO;
    
    private Page<CourseDTO> coursePage;
    private PageDTO pageDTO;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO(1L, "Java Basics", "Introduction to Java", "http://example.com/java", "Weekly",
                new BigDecimal("100.00"), new BigDecimal("80.00"), 1L, true);
        course = new Course(1L, "Java Basics", "Introduction to Java", "http://example.com/java", "Weekly",
                new BigDecimal("100.00"), new BigDecimal("80.00"), new Category(1L, "Programming"), true);
        
        // Mock service behavior for delete
        doNothing().when(courseService).delete(anyLong());
        
        coursePage = new PageImpl<>(Collections.singletonList(courseDTO), PageRequest.of(0, 10), 1);        
        PaginationDTO paginationDTO = PaginationDTO.builder()
                .numberOfElement(coursePage.getNumberOfElements())
                .number(coursePage.getNumber())
                .size(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .empty(coursePage.isEmpty())
                .first(coursePage.isFirst())
                .last(coursePage.isLast())
                .build();
        
        pageDTO = new PageDTO();
        pageDTO.setList(Collections.singletonList(courseDTO));
        pageDTO.setPagination(paginationDTO);
    }

    @Test
    void testCreateCourseSuccessfully() throws Exception {
        // Mocking service behavior
        when(courseService.save(any(CourseDTO.class))).thenReturn(courseDTO);

	        // Perform the POST request and expect a 201 CREATED status
	        mockMvc.perform(post("/api/v1/course")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(courseDTO)))
	                .andExpect(status().isCreated())
	                .andReturn();
	}
    
    @Test
    void testGetByIdSuccessfully() throws Exception {
        // Mocking service and mapper behavior
        when(courseService.getById(anyLong())).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        // Perform the GET request and expect a 200 OK status
        mockMvc.perform(get("/api/v1/course/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseDTO)));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        // Mocking service to throw ResourceNotFoundException
        when(courseService.getById(anyLong())).thenThrow(new ResourceNotFoundException("Course", "id", 1L, HttpStatus.NOT_FOUND));

        // Perform the GET request and expect a 404 Not Found status
        mockMvc.perform(get("/api/v1/course/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateSuccessfully() throws Exception {
        // Mocking service behavior
        when(courseService.update(anyLong(), any(CourseDTO.class))).thenReturn(courseDTO);

        // Perform the PUT request and expect a 200 OK status
        mockMvc.perform(put("/api/v1/course/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseDTO)));
    }

    @Test
    void testUpdateCourseNotFound() throws Exception {
        // Mocking service to throw ResourceNotFoundException
        when(courseService.update(anyLong(), any(CourseDTO.class)))
                .thenThrow(new ResourceNotFoundException("Course", "id", 1L, HttpStatus.NOT_FOUND));

        // Perform the PUT request and expect a 404 Not Found status
        mockMvc.perform(put("/api/v1/course/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteByIdSuccessfully() throws Exception {
        // Perform the DELETE request and expect a 200 OK status
        mockMvc.perform(delete("/api/v1/course/{id}", 1L))
                .andExpect(status().isOk());

        // Verify the service delete method was called
        verify(courseService).delete(1L);
    }

    @Test
    void testDeleteByIdThrowsResourceNotFoundException() throws Exception {
        // Mock service to throw ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Course", "id", 1L, HttpStatus.NOT_FOUND))
                .when(courseService).delete(anyLong());

        // Perform the DELETE request and expect a 404 Not Found status
        mockMvc.perform(delete("/api/v1/course/{id}", 1L))
                .andExpect(status().isNotFound());

        // Verify the service delete method was called
        verify(courseService).delete(1L);
    }
    
    @Test
    void testGetCoursesListSuccessfully() throws Exception {
        // Mocking service and mapper behavior
        when(courseService.getCourses(any(Map.class))).thenReturn(coursePage);
        when(pageMapper.toDTO(coursePage)).thenReturn(pageDTO);

        // Perform the GET request and expect a 200 OK status
        mockMvc.perform(get("/api/v1/course")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageDTO)));
    }
}

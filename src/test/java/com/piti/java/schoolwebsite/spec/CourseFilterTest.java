package com.piti.java.schoolwebsite.spec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class CourseFilterTest {
	@Test
    public void testCourseFilter() {
        // Create an instance of CourseFilter
        CourseFilter courseFilter = new CourseFilter();
        
        // Set values
        courseFilter.setCourseId(1);
        courseFilter.setCourseName("Math 101");
        courseFilter.setCategoryId(2);
        courseFilter.setCategoryName("Mathematics");
        
        // Assert values
        assertEquals(1, courseFilter.getCourseId());
        assertEquals("Math 101", courseFilter.getCourseName());
        assertEquals(2, courseFilter.getCategoryId());
        assertEquals("Mathematics", courseFilter.getCategoryName());
    }

    @Test
    public void testCourseFilterDefaultConstructor() {
        // Create an instance of CourseFilter using default constructor
        CourseFilter courseFilter = new CourseFilter();
        
        // Assert default values (null or default)
        assertNull(courseFilter.getCourseId());
        assertNull(courseFilter.getCourseName());
        assertNull(courseFilter.getCategoryId());
        assertNull(courseFilter.getCategoryName());
    }

    @Test
    public void testCourseFilterAllArgsConstructor() {
        // Create an instance of CourseFilter using all-args constructor
        CourseFilter courseFilter = new CourseFilter(1, "Math 101", 2, "Mathematics");
        
        // Assert values
        assertEquals(1, courseFilter.getCourseId());
        assertEquals("Math 101", courseFilter.getCourseName());
        assertEquals(2, courseFilter.getCategoryId());
        assertEquals("Mathematics", courseFilter.getCategoryName());
    }
}

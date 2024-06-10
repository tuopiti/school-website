package com.piti.java.schoolwebsite.spec;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.model.Course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
public class CourseSpecTest {
	 @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private Root<Course> courseRoot;

    @Mock
    private Join<Course, Category> categoryJoin;

    /*
    @Test
    public void testToPredicate() {
        // Given
        CourseFilter filter = new CourseFilter();
        filter.setCourseId(1);
        filter.setCourseName("Math 101");
        filter.setCategoryId(2);
        filter.setCategoryName("Mathematics");

        CourseSpec spec = new CourseSpec(filter);

        doReturn(categoryJoin).when(courseRoot).join(anyString());
        when(cb.like(any(Path.class), anyString())).thenReturn(mock(Predicate.class));

        // When
        Predicate predicate = spec.toPredicate(courseRoot, query, cb);

        // Then
        assertNotNull(predicate);
       
    }
    */
}

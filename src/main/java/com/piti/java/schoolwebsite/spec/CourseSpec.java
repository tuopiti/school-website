package com.piti.java.schoolwebsite.spec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.piti.java.schoolwebsite.model.Category;
import com.piti.java.schoolwebsite.model.Course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
public class CourseSpec implements Specification<Course>{
	private final CourseFilter courseFilter;

	@Override
	public Predicate toPredicate(Root<Course> course, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> list = new ArrayList<>();
		Join<Course, Category> category = course.join("category");
		
		if(courseFilter.getCourseId() != null) {
			Predicate courseId = course.get("id").in(courseFilter.getCourseId());
			list.add(courseId);
		}
		
		if(courseFilter.getCourseName() != null) {
	        Predicate hotelName = cb.like(course.get("name"), "%" + courseFilter.getCourseName() + "%");
	        list.add(hotelName);
	    }
		
		if(courseFilter.getCategoryId() != null) {
        	Predicate locationId = category.get("id").in(courseFilter.getCategoryId());
        	list.add(locationId);
        }
		
		if(courseFilter.getCategoryName() != null) {
	        Predicate locationName = cb.like(category.get("name"), "%" + courseFilter.getCategoryName() + "%");
	        list.add(locationName);
	    }
		
		
		Predicate[] predicates = list.toArray(Predicate[]::new);
		return cb.and(predicates);
	}

}

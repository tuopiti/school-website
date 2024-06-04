package com.piti.java.schoolwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piti.java.schoolwebsite.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

}

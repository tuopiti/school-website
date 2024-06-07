package com.piti.java.schoolwebsite.spec;

import lombok.Data;

@Data
public class CourseFilter {
	private Integer courseId;
	private String courseName;
	private Integer categoryId;
	private String categoryName;
}

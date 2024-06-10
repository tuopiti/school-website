package com.piti.java.schoolwebsite.spec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseFilter {
	private Integer courseId;
	private String courseName;
	private Integer categoryId;
	private String categoryName;
}

package com.piti.java.schoolwebsite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRegisterDTO {
	@NotNull(message = "Course ID cannot be null")
	private Long courseId;
}

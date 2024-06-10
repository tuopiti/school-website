package com.piti.java.schoolwebsite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRegisterDTO {
	@NotNull(message = "Course ID cannot be null")
	private Long courseId;
}

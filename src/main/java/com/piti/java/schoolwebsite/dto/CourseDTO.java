package com.piti.java.schoolwebsite.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
	private Long id;
	
	@NotBlank(message = "Course name is mandatory")
	@Size(max = 100, message = "Course name must be less than 100 characters")
	private String name;
	
	@NotBlank(message = "Description is mandatory")
	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;
	
	@NotBlank(message = "Video URL is mandatory")
    @Pattern(regexp = "^(http|https)://.*$", message = "Video URL must be a valid URL")
	private String videoUrl;
	
	@NotBlank(message = "Meeting schedule is mandatory")
	private String meetingSchedule;
	
	@NotNull(message = "Tuition fee for students is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tuition fee for students must be greater than 0")
	private BigDecimal tuitionFeeStudent;
	
	@NotNull(message = "Tuition fee for employees is mandatory")
	@DecimalMin(value = "0.0", inclusive = false, message = "Tuition fee for employees must be greater than 0")
	private BigDecimal tuitionFeeEmployee;
	
	@NotNull(message = "Category id must not be null")
	@Min(value = 1, message = "Category ID must be a positive number")
	private long categoryId;
	
	@NotNull(message = "Discount Eligible Type is mandatory")
	private boolean isDiscountEligible;
}

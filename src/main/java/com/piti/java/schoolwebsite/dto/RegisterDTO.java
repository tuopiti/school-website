package com.piti.java.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.piti.java.schoolwebsite.enums.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
	private Integer id;

	@NotEmpty(message = "Course list cannot be empty")
	@Valid
	private List<CourseRegisterDTO> courses;
	
	@NotNull(message = "User ID cannot be null")
	private Long userId;
	private Long promotionId;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	//@NotNull(message = "Payment amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Payment amount must be positive")
	private BigDecimal paymentAmount;
    
    private BigDecimal initialPayment;
    private String paymentMethod;
	
	@NotNull(message = "Register date cannot be null")
    //@FutureOrPresent(message = "Register date cannot be in the past")
	private LocalDateTime registerDate;
	
	private List<PaymentDTO> payments;
	
	public List<Long> getCourseIds() {
        return courses.stream()
                      .map(CourseRegisterDTO::getCourseId)
                      .collect(Collectors.toList());
    }
	
}

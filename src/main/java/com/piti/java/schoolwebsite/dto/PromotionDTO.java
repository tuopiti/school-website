package com.piti.java.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.validator.EndDateAfterStartDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EndDateAfterStartDate(message = "End date must be after start date")
public class PromotionDTO {
	private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    @NotNull(message = "The number of courses must not be null")
    @Min(value = 1, message = "The number of courses must be at least 1")
    private Integer numberOfCourses;

    //@NotNull(message = "Discount percentage is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount percentage must be less than or equal to 100")
    private BigDecimal discountPercentage;

    //@NotNull(message = "Discount amount is mandatory")
    @DecimalMin(value = "0.0", message = "Discount amount must be greater than or equal to 0")
    //@DecimalMin(value = "0.0", inclusive = false, message = "Discount amount must be greater than 0")
    private BigDecimal discountAmount;

    //@NotNull(message = "Extra courses is mandatory")
    @Min(value = 0, message = "Extra courses must be greater than or equal to 0")
    private Integer extraCourses;
    
    @Min(value = 0, message = "Pay only number of courses must be greater than or equal to 0")
    private Integer payOnlyNumberCourses;

    @NotNull(message = "Start date is mandatory")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;
    
    @NotNull(message = "Promotion Type is mandatory")
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType; 

}

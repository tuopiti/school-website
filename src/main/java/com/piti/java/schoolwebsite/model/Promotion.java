package com.piti.java.schoolwebsite.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.piti.java.schoolwebsite.enums.PromotionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotions")
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private Integer numberOfCourses;
	private BigDecimal discountPercentage;
	private BigDecimal discountAmount;
	private Integer extraCourses;
	private LocalDate startDate;
	private LocalDate endDate;
	
	@Enumerated(EnumType.STRING)
	private PromotionType promotionType; // New field to indicate the type of promotion
}

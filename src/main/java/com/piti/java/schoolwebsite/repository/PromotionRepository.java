package com.piti.java.schoolwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long>{
	@Query("SELECT COUNT(p) FROM Promotion p WHERE " +
		       "(p.numberOfCourses = :numberOfCourses AND p.promotionType = :promotionType)")
	Long existsPromotion(@Param("numberOfCourses") Integer numberOfCourses,
							@Param("promotionType") PromotionType promotionType);
	
	
	@Query("SELECT p FROM Promotion p WHERE p.numberOfCourses = :numberOfCourses AND p.promotionType = :promotionType")
	Promotion getPromotion(@Param("numberOfCourses") Integer numberOfCourses, 
	    				@Param("promotionType") PromotionType promotionType);
}

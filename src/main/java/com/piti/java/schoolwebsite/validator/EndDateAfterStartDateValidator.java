package com.piti.java.schoolwebsite.validator;

import com.piti.java.schoolwebsite.dto.PromotionDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, PromotionDTO>{
	
    @Override
    public boolean isValid(PromotionDTO promotionDTO, ConstraintValidatorContext context) {
        if (promotionDTO.getStartDate() == null || promotionDTO.getEndDate() == null) {
            return true;
        }
        return promotionDTO.getEndDate().isAfter(promotionDTO.getStartDate());
    }
}

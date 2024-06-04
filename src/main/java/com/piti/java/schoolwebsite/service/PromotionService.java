package com.piti.java.schoolwebsite.service;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.model.Promotion;

public interface PromotionService {
	PromotionDTO save(PromotionDTO promotionDTO);
	Promotion getById(Long id);
}

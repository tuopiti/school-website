package com.piti.java.schoolwebsite.service;

import java.util.List;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.model.Promotion;

public interface PromotionService {
	PromotionDTO save(PromotionDTO promotionDTO);
	Promotion getById(Long id);
	PromotionDTO update(Long id, PromotionDTO promotionDTO);
	void delete(Long id);
	List<PromotionDTO> getPromotions();
}

package com.piti.java.schoolwebsite.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.PromotionMapper;
import com.piti.java.schoolwebsite.model.Promotion;
import com.piti.java.schoolwebsite.repository.PromotionRepository;
import com.piti.java.schoolwebsite.service.PromotionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PromotionServiceImpl implements PromotionService{
	private PromotionRepository promotionRepository;

	@Override
	public PromotionDTO save(PromotionDTO promotionDTO) {
		Promotion promotion = PromotionMapper.INSTANCE.toEntity(promotionDTO);
		promotion = promotionRepository.save(promotion);
		return PromotionMapper.INSTANCE.toDTO(promotion);
	}

	@Override
	public Promotion getById(Long id) {
		return promotionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", id, HttpStatus.NOT_FOUND)); 
	}

}

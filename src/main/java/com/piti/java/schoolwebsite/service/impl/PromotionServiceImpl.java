package com.piti.java.schoolwebsite.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.exception.ApiException;
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
	private PromotionMapper promotionMapper;

	@Override
	public PromotionDTO save(PromotionDTO promotionDTO) {
		if (isPromotionExists(promotionDTO)>= 1) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Promotion is already exists");
		}
		Promotion promotion = promotionMapper.toEntity(promotionDTO);
		promotion = promotionRepository.save(promotion);
		return promotionMapper.toDTO(promotion);
	}
	
	private Long isPromotionExists(PromotionDTO promotionDTO) {
	    Integer numberOfCourses = promotionDTO.getNumberOfCourses();
	    PromotionType promotionType = promotionDTO.getPromotionType();
	    return promotionRepository.existsPromotion(numberOfCourses, promotionType);
	}

	@Override
	public Promotion getById(Long id) {
		return promotionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", id, HttpStatus.NOT_FOUND)); 
	}

	@Override
	public PromotionDTO update(Long id, PromotionDTO promotionDTO) {
		if (isPromotionExists(promotionDTO)>= 1) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Promotion is already exists");
		}
		
		Promotion exitsPromotion = getById(id);
		promotionMapper.updatePromotionFromDTO(promotionDTO, exitsPromotion);
		Promotion updatedPromotion = promotionRepository.save(exitsPromotion);
		return promotionMapper.toDTO(updatedPromotion);
	}

	@Override
	public void delete(Long id) {
		Promotion promotion = getById(id);
		promotionRepository.delete(promotion);
	}

	@Override
	public List<PromotionDTO> getPromotions() {
		List<Promotion> promotions = promotionRepository.findAll();
		return promotions.stream()
                .map(promotionMapper::toDTO)
                .collect(Collectors.toList());
	}

}

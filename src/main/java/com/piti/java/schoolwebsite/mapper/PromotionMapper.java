package com.piti.java.schoolwebsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.model.Promotion;

@Mapper
public interface PromotionMapper {
	PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
	
	Promotion toEntity(PromotionDTO promotionDTO);
	PromotionDTO toDTO(Promotion promotion);
}

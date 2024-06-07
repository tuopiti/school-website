package com.piti.java.schoolwebsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.model.Promotion;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
	//PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
	
	Promotion toEntity(PromotionDTO promotionDTO);
	PromotionDTO toDTO(Promotion promotion);
	
	@Mapping(target = "id", ignore = true)
	void updatePromotionFromDTO(PromotionDTO promotionDTO, @MappingTarget Promotion promotion);
}

package com.piti.java.schoolwebsite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.mapper.PromotionMapper;
import com.piti.java.schoolwebsite.model.Promotion;
import com.piti.java.schoolwebsite.service.PromotionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/promotion")
@AllArgsConstructor
public class PromotionController {
	private PromotionService promotionService;
	
	@PostMapping
	public ResponseEntity<PromotionDTO> create(@RequestBody @Valid PromotionDTO promotionDTO){
		PromotionDTO dto = promotionService.save(promotionDTO);
		return new ResponseEntity<PromotionDTO>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PromotionDTO> getById(@PathVariable("id") Long id){
		Promotion promotion = promotionService.getById(id);
		return ResponseEntity.ok(PromotionMapper.INSTANCE.toDTO(promotion));
	}

}

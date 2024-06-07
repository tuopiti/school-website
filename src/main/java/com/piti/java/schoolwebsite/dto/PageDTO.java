package com.piti.java.schoolwebsite.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageDTO {
	private List<?> list;
	private PaginationDTO pagination;
	
	/*
	public PageDTO(Page<?> page) {
		this.pagination = new PaginationDTO();
		
		this.list = page.getContent();
		this.pagination.setEmpty(page.isEmpty());
		this.pagination.setFirst(page.isFirst());
		this.pagination.setLast(page.isLast());
		this.pagination.setNumber(page.getNumber());
		this.pagination.setNumberOfElement(page.getNumberOfElements());
		this.pagination.setSize(page.getSize());
		this.pagination.setTotalElements(page.getTotalElements());
		this.pagination.setTotalPages(page.getTotalPages());
	}
	*/
	
}

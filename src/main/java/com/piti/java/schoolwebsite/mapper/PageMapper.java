package com.piti.java.schoolwebsite.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.piti.java.schoolwebsite.dto.PageDTO;
import com.piti.java.schoolwebsite.dto.PaginationDTO;

@Mapper(componentModel = "spring")
public interface PageMapper {
	//PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);
	
	@Mapping(target = "pagination", expression = "java(toPaginationDTO(page))")
	@Mapping(target = "list", expression = "java(page.getContent())")
	PageDTO toDTO(Page<?> page);
	
	PaginationDTO toPaginationDTO(Page<?> page);
}

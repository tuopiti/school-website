package com.piti.java.schoolwebsite.utils;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface PageUtils {
	int PAGE_SIZE_DEAFAULT = 2;
	int PAGE_NUMBER_DEFAULT = 1;
	//String SORT_BY_DEFAULT = "id";
	//String SORT_ORDER_DEFAULT = "asc";
	String PAGE_SIZE = "_limit";
	String PAGE_NUMBER = "_page";
	String SORT_BY = "_sort";
    String SORT_ORDER = "_order";
	
	static Pageable getPageable(Map<String, String> params) {
		int pageSize = MapUtils.getInteger(params, PAGE_SIZE, PAGE_SIZE_DEAFAULT);  
		int pageNumber = MapUtils.getInteger(params, PAGE_NUMBER, PAGE_NUMBER_DEFAULT);
		
		String sortBy = params.getOrDefault(SORT_BY, null);
	    String sortOrder = params.getOrDefault(SORT_ORDER, null);
		if(pageNumber < 0) {
			pageNumber = PAGE_NUMBER_DEFAULT;
		}
		if(pageSize < 0) {
			pageSize = PAGE_SIZE_DEAFAULT;
		}
		
		//Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
		
	    Sort.Direction direction = Sort.Direction.ASC;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
			
		return pageable;
	}
}

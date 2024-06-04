package com.piti.java.schoolwebsite.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
	private String resourceName;
	private String fieldName;
	private Long fieldValue;
	private HttpStatus status;
	
	public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue, HttpStatus status){
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.status = status;
	}
}

package com.piti.java.schoolwebsite.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.model.Register;

public interface RegisterService{
	void registerCourse(RegisterDTO registerDTO);
	Register getRegisterIdWithPayments(Long registerId);
	Page<RegisterDTO> getRegisters(Map<String, String> params);
	List<RegisterDTO> getRegistrationsByUser(Long userId);
}

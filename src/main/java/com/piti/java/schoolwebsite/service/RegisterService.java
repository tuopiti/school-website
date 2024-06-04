package com.piti.java.schoolwebsite.service;

import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.model.Register;

public interface RegisterService{
	void registerCourse(RegisterDTO registerDTO);
	Register getRegisterIdWithPayments(Long registerId);
}

package com.piti.java.schoolwebsite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.PageDTO;
import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.mapper.PageMapper;
import com.piti.java.schoolwebsite.mapper.RegisterMapper;
import com.piti.java.schoolwebsite.model.Register;
import com.piti.java.schoolwebsite.service.RegisterService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/register")
@AllArgsConstructor
public class RegisterController {
	private RegisterService registerService;
	private RegisterMapper registerMapper;
	private PageMapper pageMapper;
	
	@PostMapping
    public ResponseEntity<?> registerCourse(@RequestBody @Valid RegisterDTO registerDTO) {        
		registerService.registerCourse(registerDTO);
        return ResponseEntity.ok().build();
    }
	
	
	@GetMapping("/{id}")
    public ResponseEntity<RegisterDTO> getRegisterWithPayments(@PathVariable("id") Long id) {
        Register register = registerService.getRegisterIdWithPayments(id);
        return ResponseEntity.ok(registerMapper.toDTO(register));
    }
	
	
	@GetMapping
	public ResponseEntity<?> getRegistersList(@RequestParam Map<String, String> params){
		Page<RegisterDTO> registers = registerService.getRegisters(params);
		PageDTO pageDTO = pageMapper.toDTO(registers);
		return ResponseEntity.ok(pageDTO);
	}
	
	
	@GetMapping("/user/{userId}")
    public ResponseEntity<List<RegisterDTO>> getRegistrationsByUser(@PathVariable Long userId) {
         List<RegisterDTO> registrationsByUser = registerService.getRegistrationsByUser(userId);
        return ResponseEntity.ok(registrationsByUser);
    }
    
}

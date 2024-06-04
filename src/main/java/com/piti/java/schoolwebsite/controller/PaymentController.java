package com.piti.java.schoolwebsite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.PaymentDTO;
import com.piti.java.schoolwebsite.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentController {
	private PaymentService paymentService;
	
	@PostMapping
    public ResponseEntity<String> processPayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.processPayment(paymentDTO);
        return new ResponseEntity<>("Payment processed successfully", HttpStatus.OK);
    }
}

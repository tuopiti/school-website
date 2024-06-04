package com.piti.java.schoolwebsite.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.PaymentDTO;
import com.piti.java.schoolwebsite.enums.PaymentStatus;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.model.Payment;
import com.piti.java.schoolwebsite.model.Register;
import com.piti.java.schoolwebsite.repository.PaymentRepository;
import com.piti.java.schoolwebsite.repository.RegisterRepository;
import com.piti.java.schoolwebsite.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{
	private final RegisterRepository registerRepository;
    private final PaymentRepository paymentRepository;
    
    @Transactional
	@Override
	public void processPayment(PaymentDTO paymentDTO) {
		Register register = registerRepository.findById(paymentDTO.getRegisterId())
                .orElseThrow(() -> new ResourceNotFoundException("Register", "id", paymentDTO.getRegisterId(), HttpStatus.NOT_FOUND));

        Payment payment = new Payment();
        payment.setRegister(register);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setFullPayment(paymentDTO.isFullPayment());

        paymentRepository.save(payment);

        if (paymentDTO.isFullPayment()) {
            register.setPaymentStatus(PaymentStatus.PAID);
            
        } else {
        	
            BigDecimal totalPaid = register.getPayments().stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalPaid.compareTo(register.getPaymentAmount()) >= 0) {
                register.setPaymentStatus(PaymentStatus.PAID);
                payment.setFullPayment(true);
                paymentRepository.save(payment);
                
            } else {
                register.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
            }
            
        }
        
        registerRepository.save(register);
		
	}
}

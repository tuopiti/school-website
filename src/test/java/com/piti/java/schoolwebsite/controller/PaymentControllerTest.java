package com.piti.java.schoolwebsite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.PaymentDTO;
import com.piti.java.schoolwebsite.service.PaymentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessPayment() throws Exception {
        // Create a test PaymentDTO
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRegisterId(1L);
        paymentDTO.setAmount(new BigDecimal("100.00"));
        paymentDTO.setPaymentDate(LocalDateTime.now());
        paymentDTO.setPaymentMethod("Credit Card");
        paymentDTO.setFullPayment(true);

        // Convert the PaymentDTO to JSON
        String paymentDTOJson = objectMapper.writeValueAsString(paymentDTO);

        // Perform a POST request to /api/v1/payment
        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentDTOJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully"));

        // Verify that the service method was called once
        verify(paymentService, times(1)).processPayment(any(PaymentDTO.class));
    }
}

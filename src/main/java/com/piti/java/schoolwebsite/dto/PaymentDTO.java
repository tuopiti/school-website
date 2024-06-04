package com.piti.java.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long registerId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private boolean isFullPayment;
}

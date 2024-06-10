package com.piti.java.schoolwebsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.piti.java.schoolwebsite.dto.PaymentDTO;
import com.piti.java.schoolwebsite.enums.PaymentStatus;
import com.piti.java.schoolwebsite.model.Payment;
import com.piti.java.schoolwebsite.model.Register;
import com.piti.java.schoolwebsite.repository.PaymentRepository;
import com.piti.java.schoolwebsite.repository.RegisterRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PaymentServiceTest {	
	@Autowired
    private PaymentService paymentService;

    @MockBean
    private RegisterRepository registerRepository;

    @MockBean
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        // Setup code, if necessary
    }

    @Test
    void testProcessPayment() {
        // Set up test data
        PaymentDTO paymentDTO = createTestPaymentDTO();
        Register register = createTestRegister();
        register.setPayments(new ArrayList<>());

        // Mock the behavior of the registerRepository to return the test register
        when(registerRepository.findById(anyLong())).thenReturn(Optional.of(register));

        // Mock the behavior of the paymentRepository.save() method to add a payment to the register
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            register.getPayments().add(payment);
            return payment;
        });

        // Invoke the method under test
        paymentService.processPayment(paymentDTO);

        // Verify the payment was processed correctly
        verify(paymentRepository, times(1)).save(any(Payment.class));

        // Get the payment from the register
        BigDecimal totalPaid = BigDecimal.ZERO;
        List<Payment> payments = register.getPayments();
        if (payments != null && !payments.isEmpty()) {
            totalPaid = payments.stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        if (totalPaid.compareTo(register.getPaymentAmount()) >= 0) {
            register.setPaymentStatus(PaymentStatus.PAID);
            Payment payment = register.getPayments().get(register.getPayments().size() - 1); // Get the last payment
            payment.setFullPayment(true);
            paymentRepository.save(payment);
        } else {
            register.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        // Assert the expected outcome
        assertEquals(PaymentStatus.PAID, register.getPaymentStatus());
        assertTrue(register.getPayments().get(register.getPayments().size() - 1).isFullPayment());
        // Add more assertions as needed
    }

    private PaymentDTO createTestPaymentDTO() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRegisterId(1L);
        paymentDTO.setAmount(new BigDecimal("100.00"));
        paymentDTO.setPaymentDate(LocalDateTime.now());
        paymentDTO.setPaymentMethod("Credit Card");
        paymentDTO.setFullPayment(true);
        return paymentDTO;
    }

    private Register createTestRegister() {
        Register register = new Register();
        register.setId(1L);
        register.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        register.setPaymentAmount(new BigDecimal("100.00"));
        // Add more properties as needed
        return register;
    }
}

package com.piti.java.schoolwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piti.java.schoolwebsite.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}

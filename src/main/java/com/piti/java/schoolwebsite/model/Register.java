package com.piti.java.schoolwebsite.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.piti.java.schoolwebsite.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "registers")
@NoArgsConstructor
@AllArgsConstructor
public class Register {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    private User user;
	
	@ManyToOne
    private Promotion promotion;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	private BigDecimal paymentAmount;
	
	private BigDecimal initialPayment;
	private String paymentMethod;
	
	@Column(name = "register_date")
	private LocalDateTime registerDate;
	
	@OneToMany(mappedBy = "register", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegisterDetail> registerDetails;
	
	@OneToMany(mappedBy = "register", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}

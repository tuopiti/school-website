package com.piti.java.schoolwebsite.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piti.java.schoolwebsite.dto.CourseRegisterDTO;
import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.enums.PaymentStatus;
import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.enums.Role;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.RegisterMapper;
import com.piti.java.schoolwebsite.model.Course;
import com.piti.java.schoolwebsite.model.Payment;
import com.piti.java.schoolwebsite.model.Promotion;
import com.piti.java.schoolwebsite.model.Register;
import com.piti.java.schoolwebsite.model.RegisterDetail;
import com.piti.java.schoolwebsite.model.User;
import com.piti.java.schoolwebsite.repository.CourseRepository;
import com.piti.java.schoolwebsite.repository.PaymentRepository;
import com.piti.java.schoolwebsite.repository.PromotionRepository;
import com.piti.java.schoolwebsite.repository.RegisterDetailRepository;
import com.piti.java.schoolwebsite.repository.RegisterRepository;
import com.piti.java.schoolwebsite.repository.UserRepository;
import com.piti.java.schoolwebsite.service.RegisterService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService{
	private UserRepository userRepository;
	private CourseRepository courseRepository;
	private PromotionRepository promotionRepository;
	private RegisterRepository registerRepository;
	private RegisterDetailRepository registerDetailRepository;
	private RegisterMapper registerMapper;
	private PaymentRepository paymentRepository;

	@Override
	@Transactional
	public void registerCourse(RegisterDTO registerDTO) {
		List<CourseRegisterDTO> courses = registerDTO.getCourses();
		List<Long> courseIds = registerDTO.getCourseIds();
		
		User user = userRepository.findById(registerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", registerDTO.getUserId(), HttpStatus.NOT_FOUND));
		
		Promotion promotion = promotionRepository.findById(registerDTO.getPromotionId())
		.orElseThrow(() -> new ResourceNotFoundException("Promotion", "id", registerDTO.getPromotionId(), HttpStatus.NOT_FOUND));
		
		BigDecimal totalTuitionFee = BigDecimal.ZERO;
		int discountEligibleCoursesCount = 0;
		
		for (Long courseId : courseIds) {
			Course course = courseRepository.findById(courseId)
					.orElseThrow(() -> new ResourceNotFoundException("User", "id", courseId, HttpStatus.NOT_FOUND));
			
			if(course.isDiscountEligible()) {
				discountEligibleCoursesCount = discountEligibleCoursesCount + 1;
				
			}
			// TuitionFee By Role
			if(user.getRole() == Role.STUDENT) {
				totalTuitionFee = totalTuitionFee.add(course.getTuitionFeeStudent());
			}else if (user.getRole() == Role.EMPLOYEE){
				totalTuitionFee = totalTuitionFee.add(course.getTuitionFeeEmployee());
			}
			
		}
		
		if (isPromotionValid(promotion)) {
			if(discountEligibleCoursesCount >= promotion.getNumberOfCourses()) {
				totalTuitionFee = applyPromotion(totalTuitionFee, promotion, user, courseIds);
			}
		}
		
		
		registerDTO.setPaymentAmount(totalTuitionFee);	
		
		//save register
		Register register = registerMapper.toRegister(registerDTO);
		registerRepository.save(register);
		
		//save register detail
		for (CourseRegisterDTO courseRegisterDTOs: courses) {
			RegisterDetail registerDetail = registerMapper.toRegisterDetail(courseRegisterDTOs, register);
			registerDetailRepository.save(registerDetail);
		}
		
		// Process initial payment (if provided)
		processInitialPayment(registerDTO, totalTuitionFee, register);
		
	}


	private void processInitialPayment(RegisterDTO registerDTO, BigDecimal totalTuitionFee, Register register) {		
        if (registerDTO.getInitialPayment() != null) {
            Payment initialPayment = new Payment();
            initialPayment.setRegister(register);
            initialPayment.setAmount(registerDTO.getInitialPayment());
            initialPayment.setPaymentDate(LocalDateTime.now());
            initialPayment.setPaymentMethod(registerDTO.getPaymentMethod());
            initialPayment.setFullPayment(false);

            paymentRepository.save(initialPayment);

            BigDecimal totalPaid = initialPayment.getAmount();
            if (totalPaid.compareTo(totalTuitionFee) >= 0) {
                register.setPaymentStatus(PaymentStatus.PAID);
                initialPayment.setFullPayment(true);
                paymentRepository.save(initialPayment);
            } else {
                register.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
            }

            registerRepository.save(register);
        }
	}
	
	
	private boolean isPromotionValid(Promotion promotion) {
        LocalDate today = LocalDate.now();
        return today.isEqual(promotion.getStartDate()) || today.isBefore(promotion.getEndDate());
	}
	
	
	private BigDecimal applyPromotion(BigDecimal totalTuitionFee, Promotion promotion, User user, List<Long> courseIds) {
        if (promotion.getPromotionType() == PromotionType.DISCOUNT_PERCENTAGE && promotion.getDiscountPercentage() != null) {
            return totalTuitionFee.multiply(BigDecimal.ONE.subtract(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
            
        } else if (promotion.getPromotionType() == PromotionType.DISCOUNT_AMOUNT && promotion.getDiscountAmount() != null) {
            return totalTuitionFee.subtract(promotion.getDiscountAmount());
            
        } else if (promotion.getPromotionType() == PromotionType.PAY_ONLY_N_COURSES) {
        	
            // Apply "Register 3 courses, pay only 2" promotion
            BigDecimal highestCourseFee = BigDecimal.ZERO;
            for (Long courseId : courseIds) {
                Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId, HttpStatus.NOT_FOUND));
                BigDecimal courseFee = user.getRole() == Role.STUDENT ? course.getTuitionFeeStudent() : course.getTuitionFeeEmployee();
                if (course.isDiscountEligible() && courseFee.compareTo(highestCourseFee) > 0) {
                    highestCourseFee = courseFee;
                }
            }
            totalTuitionFee = totalTuitionFee.subtract(highestCourseFee);
        }
        return totalTuitionFee;
	}

   
	@Transactional(readOnly = true)
	@Override
	public Register getRegisterIdWithPayments(Long registerId) {
		return registerRepository.findById(registerId)
                .orElseThrow(() -> new ResourceNotFoundException("Register", "id", registerId, HttpStatus.NOT_FOUND));
		
	}
	
	
}

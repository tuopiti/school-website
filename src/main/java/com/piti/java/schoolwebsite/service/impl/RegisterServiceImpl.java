package com.piti.java.schoolwebsite.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.piti.java.schoolwebsite.service.PromotionService;
import com.piti.java.schoolwebsite.service.RegisterService;
import com.piti.java.schoolwebsite.utils.PageUtils;

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
	private PromotionService promotionService;

	@Override
	@Transactional
	public void registerCourse(RegisterDTO registerDTO) {
		List<CourseRegisterDTO> courses = registerDTO.getCourses();
		List<Long> courseIds = registerDTO.getCourseIds();
		PromotionType promotionType = registerDTO.getPromotionType();
		//List<Long> extraCourseIds = registerDTO.getExtraCourseIds();
		List<Long> extraCourseIds = promotionType == PromotionType.EXTRA_COURSES ? registerDTO.getExtraCourseIds() : Collections.emptyList();
		
		User user = userRepository.findById(registerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", registerDTO.getUserId(), HttpStatus.NOT_FOUND));
		
		BigDecimal totalTuitionFee = BigDecimal.ZERO;
		Integer discountEligibleCoursesCount = 0;
		
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
		
		 // Log the parameters
	    //System.out.println("Promotion Type: " + promotionType);
	    //System.out.println("Discount Eligible Courses Count: " + discountEligibleCoursesCount);

		Promotion promotion = promotionRepository.getPromotion(discountEligibleCoursesCount, promotionType);
		/*
		if (promotion == null) {
	        System.out.println("No promotion found for the given criteria.");
	    } else {
	        System.out.println("Promotion found: " + promotion.getName());
	    }
		*/
		
		if (promotion != null && isPromotionValid(promotion)) {
			if(discountEligibleCoursesCount >= promotion.getNumberOfCourses()) {
				totalTuitionFee = applyPromotion(totalTuitionFee, promotion, user, courseIds, extraCourseIds);
			}
			
		}
		
		registerDTO.setRegisterDate(LocalDateTime.now());
		registerDTO.setPaymentAmount(totalTuitionFee);	
		
		//save register
		Register register = registerMapper.toRegister(registerDTO);
		if (promotion != null) {
			register.setPromotion( promotionService.getById(promotion.getId()));
		}
		registerRepository.save(register);
		
		//save register detail
		for (CourseRegisterDTO courseRegisterDTOs: courses) {
			RegisterDetail registerDetail = registerMapper.toRegisterDetail(courseRegisterDTOs, register);
			registerDetailRepository.save(registerDetail);
		}
		
		// Process initial payment (if provided)
		processInitialPayment(registerDTO, totalTuitionFee, register);
		
		
		 // Filter out duplicate extra courses
	    extraCourseIds = extraCourseIds.stream()
	            .filter(extraCourseId -> !courseIds.contains(extraCourseId))
	            .collect(Collectors.toList());
	    
	    
		// Save register details for extra courses
	    if (promotionType == PromotionType.EXTRA_COURSES) {
	    	saveExtraCourses(register, extraCourseIds, promotion);
	    }
		
	}
	
	
	//save extra course
	void saveExtraCourses(Register register, List<Long> extraCourseIds, Promotion promotion) {
        if (extraCourseIds != null && !extraCourseIds.isEmpty()) {
            int extraCoursesToAdd = promotion != null ? promotion.getExtraCourses() : extraCourseIds.size();
            for (int i = 0; i < Math.min(extraCoursesToAdd, extraCourseIds.size()); i++) {
                Long extraCourseId = extraCourseIds.get(i);
                Course extraCourse = courseRepository.findById(extraCourseId)
                        .orElseThrow(() -> new ResourceNotFoundException("Course", "id", extraCourseId, HttpStatus.NOT_FOUND));
                CourseRegisterDTO extraCourseDTO = new CourseRegisterDTO();
                extraCourseDTO.setCourseId(extraCourseId);
                RegisterDetail extraRegisterDetail = registerMapper.toRegisterDetail(extraCourseDTO, register);
                if (extraRegisterDetail != null) { 
                	registerDetailRepository.save(extraRegisterDetail);
                }
            }
        }
    }

    // process Initial Payment
	void processInitialPayment(RegisterDTO registerDTO, BigDecimal totalTuitionFee, Register register) {		
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
	
	
	BigDecimal applyPromotion(BigDecimal totalTuitionFee, Promotion promotion, User user, List<Long> courseIds, List<Long> extraCourseIds) {
		try {
		    switch (promotion.getPromotionType()) {
		        case DISCOUNT_PERCENTAGE:
		            if (promotion.getDiscountPercentage() != null) {
		                return totalTuitionFee.multiply(BigDecimal.ONE.subtract(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
		            }
		            
		            break;
		            
		        case DISCOUNT_AMOUNT:
		            if (promotion.getDiscountAmount() != null) {
		                return totalTuitionFee.subtract(promotion.getDiscountAmount());
		            }
		            
		            break;
		            
		        case PAY_ONLY_N_COURSES:
		            if (promotion.getPayOnlyNumberCourses() != null && promotion.getPayOnlyNumberCourses() < courseIds.size()) {
		                // Apply "Pay Only N Courses" promotion
		                List<BigDecimal> courseFees = new ArrayList<>();
		                for (Long courseId : courseIds) {
		                    Course course = courseRepository.findById(courseId)
		                            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId, HttpStatus.NOT_FOUND));
		                    BigDecimal courseFee = user.getRole() == Role.STUDENT ? course.getTuitionFeeStudent() : course.getTuitionFeeEmployee();
		                    courseFees.add(courseFee);
		                }
		                
		                // Sort the fees in ascending order
		                courseFees.sort(BigDecimal::compareTo);
		                
		                BigDecimal payableAmount = BigDecimal.ZERO;
		                for (int i = 0; i < promotion.getPayOnlyNumberCourses(); i++) {
		                    payableAmount = payableAmount.add(courseFees.get(i));
		                }
		                
		                totalTuitionFee = payableAmount;
		            }
		            
		            break;
		            
		        case EXTRA_COURSES:
		            if (promotion.getNumberOfCourses() != null && promotion.getExtraCourses() != null) {
		               
		            }
		            
		            break;
		    }
		} catch (NullPointerException e) {
	        // Handle null pointer exception
	        e.printStackTrace();
	    }
	    return totalTuitionFee;
	    
	}
	
   
	@Transactional(readOnly = true)
	@Override
	public Register getRegisterIdWithPayments(Long registerId) {
		return registerRepository.findById(registerId)
                .orElseThrow(() -> new ResourceNotFoundException("Register", "id", registerId, HttpStatus.NOT_FOUND));
		
	}


	@Override
	public Page<RegisterDTO> getRegisters(Map<String, String> params) {
		Pageable pageable = PageUtils.getPageable(params);
		Page<Register> register = registerRepository.findAll(pageable);
		List<RegisterDTO> registerDTOs = register.getContent().stream()
						.map(registerMapper::toDTO)
						.collect(Collectors.toList());
		Page<RegisterDTO> page = new PageImpl<>(registerDTOs, pageable, register.getTotalElements());
		return page;
	}


	@Override
	public List<RegisterDTO> getRegistrationsByUser(Long userId) {
		List<Register> registers = registerRepository.findByUserId(userId);
		return registers.stream()
				.map(registerMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	
	
	
	
}

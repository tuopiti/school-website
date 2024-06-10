package com.piti.java.schoolwebsite.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest{
	
	@Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private RegisterDetailRepository registerDetailRepository;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private RegisterServiceImpl registerService;

    @Test
    void testRegisterCourse() {
        // Mock user, courses, and promotions
        User user = new User();
        user.setId(1L);
        user.setRole(Role.STUDENT);

        Course course = new Course();
        course.setId(1L);
        course.setTuitionFeeStudent(BigDecimal.valueOf(100));
        course.setTuitionFeeEmployee(BigDecimal.valueOf(200));

        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setNumberOfCourses(2);
        promotion.setPromotionType(PromotionType.EXTRA_COURSES);
        promotion.setStartDate(LocalDate.now().minusDays(1));
        promotion.setEndDate(LocalDate.now().plusDays(1));
        
        // Mock behavior of promotionService
        when(promotionService.getById(anyLong())).thenReturn(promotion);

        // Mock behavior of registerMapper
        when(registerMapper.toRegister(any(RegisterDTO.class))).thenReturn(new Register());
        
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(courseRepository.findById(any())).thenReturn(Optional.of(course));
        when(promotionRepository.getPromotion(anyInt(), any())).thenReturn(promotion);
        
        // Mock the RegisterDetail object
        RegisterDetail registerDetail = new RegisterDetail();
        when(registerMapper.toRegisterDetail(any(CourseRegisterDTO.class), any(Register.class))).thenReturn(registerDetail);

        // Create RegisterDTO and call the method
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUserId(1L);
        registerDTO.setPromotionType(PromotionType.EXTRA_COURSES);
        registerDTO.setCourses(Collections.singletonList(new CourseRegisterDTO(1L)));
        registerDTO.setExtraCourseIds(new ArrayList<>()); // Initialize to empty list

        registerService.registerCourse(registerDTO);

        // Verify that necessary methods are called
        verify(registerRepository).save(any(Register.class));
        verify(registerDetailRepository).save(any(RegisterDetail.class));
       
    }
    
     
    @Test
    void testSaveExtraCourses() {
        // Prepare test data
        Register register = new Register();
        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setExtraCourses(2);

        List<Long> extraCourseIds = Arrays.asList(1L, 2L);

        // Mock behavior of courseRepository
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));

        // Mock behavior of registerMapper
        when(registerMapper.toRegisterDetail(any(CourseRegisterDTO.class), any(Register.class)))
            .thenReturn(new RegisterDetail());

        // Call the method under test
        registerService.saveExtraCourses(register, extraCourseIds, promotion);

        // Verify behavior
        verify(courseRepository, times(2)).findById(anyLong());
        verify(registerMapper, times(2)).toRegisterDetail(any(CourseRegisterDTO.class), any(Register.class));
        verify(registerDetailRepository, times(2)).save(any(RegisterDetail.class));
    }
    
    
    @Test
    void testProcessInitialPayment() {
        // Prepare test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setInitialPayment(BigDecimal.valueOf(500)); // Example initial payment amount
        registerDTO.setPaymentMethod("Credit Card"); // Example payment method

        BigDecimal totalTuitionFee = BigDecimal.valueOf(1000); // Example total tuition fee
        Register register = new Register(); // Create a register entity

        // Mock behavior of paymentRepository
        PaymentRepository paymentRepository = mock(PaymentRepository.class);

        // Mock behavior of registerRepository
        RegisterRepository registerRepository = mock(RegisterRepository.class);

        // Create an instance of the service
        RegisterServiceImpl registerService = new RegisterServiceImpl(userRepository, courseRepository, promotionRepository, registerRepository, registerDetailRepository, registerMapper, paymentRepository, promotionService);

        // Call the method under test
        registerService.processInitialPayment(registerDTO, totalTuitionFee, register);

        // Verify behavior
        verify(paymentRepository, times(1)).save(any(Payment.class)); // Verify that payment is saved
        verify(registerRepository, times(1)).save(any(Register.class)); // Verify that register is saved

        // Verify the payment status based on the initial payment amount
        if (registerDTO.getInitialPayment().compareTo(totalTuitionFee) >= 0) {
            assertEquals(PaymentStatus.PAID, register.getPaymentStatus()); // Expect payment status to be PAID
        } else {
            assertEquals(PaymentStatus.PARTIALLY_PAID, register.getPaymentStatus()); // Expect payment status to be PARTIALLY_PAID
        }
    }
    
    
    @Test
    void testApplyPromotionDiscountPercentage() {
        // Prepare test data
        BigDecimal totalTuitionFee = BigDecimal.valueOf(1000);
        Promotion promotion = new Promotion();
        promotion.setPromotionType(PromotionType.DISCOUNT_PERCENTAGE);
        promotion.setDiscountPercentage(BigDecimal.TEN);

        // Call the method under test
        BigDecimal discountedFee = registerService.applyPromotion(totalTuitionFee, promotion, null, null, null);

        // Verify that the total tuition fee is reduced by the discount percentage
        BigDecimal expectedFee = BigDecimal.valueOf(900); // 1000 - (1000 * 10 / 100)
        assertEquals(expectedFee.setScale(0, RoundingMode.DOWN), discountedFee.setScale(0, RoundingMode.DOWN));
    }

    @Test
    void testApplyPromotionDiscountAmount() {
        // Prepare test data
        BigDecimal totalTuitionFee = BigDecimal.valueOf(1000);
        Promotion promotion = new Promotion();
        promotion.setPromotionType(PromotionType.DISCOUNT_AMOUNT);
        promotion.setDiscountAmount(BigDecimal.valueOf(200));

        // Call the method under test
        BigDecimal discountedFee = registerService.applyPromotion(totalTuitionFee, promotion, null, null, null);

        // Verify that the total tuition fee is reduced by the discount amount
        BigDecimal expectedFee = BigDecimal.valueOf(800); // 1000 - 200
        assertEquals(expectedFee, discountedFee);
    }
    
    
    @Test
    void testApplyPromotionPayOnlyNCourses() {
        // Prepare test data
        BigDecimal tuitionFee1 = BigDecimal.valueOf(200); // Tuition fee for course 1
        BigDecimal tuitionFee2 = BigDecimal.valueOf(300); // Tuition fee for course 2
        List<Long> courseIds = Arrays.asList(1L, 2L); // Example course IDs

        // Mock behavior of courseRepository
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTuitionFeeStudent(tuitionFee1);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTuitionFeeStudent(tuitionFee2);
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course2));

        // Create a promotion for paying only for 1 course
        Promotion promotion = new Promotion();
        promotion.setPromotionType(PromotionType.PAY_ONLY_N_COURSES);
        promotion.setPayOnlyNumberCourses(1);

        // Create a user
        User user = new User();
        user.setRole(Role.STUDENT); // Set user role to student

        // Call the method under test
        BigDecimal discountedFee = registerService.applyPromotion(BigDecimal.ZERO, promotion, user, courseIds, null);

        // Calculate expected fee (tuition fee of the cheapest course)
        BigDecimal expectedFee = tuitionFee1.min(tuitionFee2);

        // Verify that the discounted fee matches the expected fee
        assertEquals(expectedFee, discountedFee);
    }

    @Test
    void testApplyPromotionExtraCourses() {
        // Prepare test data
        BigDecimal totalTuitionFee = BigDecimal.valueOf(1000);
        Promotion promotion = new Promotion();
        promotion.setPromotionType(PromotionType.EXTRA_COURSES);
        promotion.setNumberOfCourses(2); // Add 2 extra courses

        List<Long> extraCourseIds = Arrays.asList(4L, 5L); // IDs of two extra courses

        // Call the method under test
        BigDecimal discountedFee = registerService.applyPromotion(totalTuitionFee, promotion, null, null, extraCourseIds);

        // Verify behavior (in this case, no changes expected as the method does nothing)
        assertEquals(totalTuitionFee, discountedFee);
    }
    
    
    @Test
    void testGetRegisterIdWithPaymentsFound() {
        // Prepare test data
        Register register = new Register();
        register.setId(1L);

        // Mock behavior of registerRepository
        when(registerRepository.findById(anyLong())).thenReturn(Optional.of(register));

        // Call the method under test
        Register result = registerService.getRegisterIdWithPayments(1L);

        // Verify behavior
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(registerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRegisterIdWithPaymentsNotFound() {
        // Mock behavior of registerRepository
        when(registerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verify exception is thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            registerService.getRegisterIdWithPayments(1L);
        });

        assertEquals("Register not found with id : '1'", exception.getMessage());
        verify(registerRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetRegisters() {
        // Prepare test data
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "10");

        Register register = new Register();
        register.setId(1L);

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setId(1L);

        List<Register> registers = Arrays.asList(register);
        Page<Register> registerPage = new PageImpl<>(registers);

        // Mock behavior of registerRepository
        when(registerRepository.findAll(any(Pageable.class))).thenReturn(registerPage);

        // Mock behavior of registerMapper
        when(registerMapper.toDTO(any(Register.class))).thenReturn(registerDTO);

        // Call the method under test
        Page<RegisterDTO> result = registerService.getRegisters(params);

        // Verify behavior
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(registerDTO, result.getContent().get(0));

        verify(registerRepository, times(1)).findAll(any(Pageable.class));
        verify(registerMapper, times(1)).toDTO(any(Register.class));
    }
    
    @Test
    void testGetRegistrationsByUser() {
        // Prepare test data
        Long userId = 1L;
        
        Register register = new Register();
        register.setId(1L);

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setId(1L);

        List<Register> registers = Arrays.asList(register);

        // Mock behavior of registerRepository
        when(registerRepository.findByUserId(anyLong())).thenReturn(registers);

        // Mock behavior of registerMapper
        when(registerMapper.toDTO(any(Register.class))).thenReturn(registerDTO);

        // Call the method under test
        List<RegisterDTO> result = registerService.getRegistrationsByUser(userId);

        // Verify behavior
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(registerDTO, result.get(0));

        verify(registerRepository, times(1)).findByUserId(userId);
        verify(registerMapper, times(1)).toDTO(register);
    }
}

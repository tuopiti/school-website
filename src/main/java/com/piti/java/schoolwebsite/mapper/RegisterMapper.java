package com.piti.java.schoolwebsite.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.piti.java.schoolwebsite.dto.CourseRegisterDTO;
import com.piti.java.schoolwebsite.dto.PaymentDTO;
import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.model.Payment;
import com.piti.java.schoolwebsite.model.Register;
import com.piti.java.schoolwebsite.model.RegisterDetail;
import com.piti.java.schoolwebsite.service.CourseService;
import com.piti.java.schoolwebsite.service.PromotionService;
import com.piti.java.schoolwebsite.service.UserService;

@Mapper(componentModel = "spring", uses = {CourseService.class, UserService.class, PromotionService.class})
public interface RegisterMapper {
	
	@Mapping(target = "user", source = "dto.userId")
	@Mapping(target = "promotion", source = "dto.promotionId")
	Register toRegister(RegisterDTO dto);
	
	@Mapping(source = "user.id", target = "userId")
    @Mapping(source = "promotion.id", target = "promotionId")
	@Mapping(target = "courses", source = "registerDetails", qualifiedByName = "mapCourses")
	@Mapping(target = "payments", source = "payments", qualifiedByName = "mapPayments")
	RegisterDTO toDTO(Register register);
	
	@Mapping(target = "register", source = "register")
	@Mapping(target = "course", source = "dto.courseId")
	@Mapping(target = "id", ignore = true)
	RegisterDetail toRegisterDetail(CourseRegisterDTO dto, Register register);
	
	@Named("mapCourses")
    default List<CourseRegisterDTO> mapCourses(List<RegisterDetail> registerDetails) {
        return registerDetails.stream()
                      .map(detail -> new CourseRegisterDTO(detail.getCourse().getId()))
                      .collect(Collectors.toList());
    }
	 
	@Named("mapPayments")
    default List<PaymentDTO> mapPayments(List<Payment> payments) {
        return payments.stream()
               .map(payment -> new PaymentDTO(
                   payment.getRegister().getId(),
                   payment.getAmount(),
                   payment.getPaymentDate(),
                   payment.getPaymentMethod(),
                   payment.isFullPayment()))
               .collect(Collectors.toList());
    }
}

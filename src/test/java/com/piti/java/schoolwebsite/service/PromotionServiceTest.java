package com.piti.java.schoolwebsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.exception.ApiException;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.PromotionMapper;
import com.piti.java.schoolwebsite.model.Promotion;
import com.piti.java.schoolwebsite.repository.PromotionRepository;
import com.piti.java.schoolwebsite.service.impl.PromotionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
	@Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private PromotionDTO promotionDTO;
    private Promotion promotion;
    
    private Promotion promotion1;
    private Promotion promotion2;
    private PromotionDTO promotionDTO1;
    private PromotionDTO promotionDTO2;

    @BeforeEach
    void setUp() {
        promotionDTO = new PromotionDTO(
                1L,
                "Summer Sale",
                "Discount on summer courses",
                5,
                new BigDecimal("10.0"),
                new BigDecimal("50.0"),
                2,
                3,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(30),
                PromotionType.DISCOUNT_PERCENTAGE
        );

        promotion = new Promotion(
                1L,
                "Summer Sale",
                "Discount on summer courses",
                5,
                new BigDecimal("10.0"),
                new BigDecimal("50.0"),
                2,
                3,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(30),
                PromotionType.DISCOUNT_PERCENTAGE
        );
        
        promotion1 = new Promotion();
        promotion1.setId(1L);
        promotion1.setName("Summer Sale");
        promotion1.setDescription("Discount on summer courses");
        promotion1.setNumberOfCourses(10);
        promotion1.setDiscountPercentage(BigDecimal.valueOf(10));
        promotion1.setDiscountAmount(BigDecimal.valueOf(50));
        promotion1.setExtraCourses(1);
        promotion1.setPayOnlyNumberCourses(9);
        promotion1.setStartDate(LocalDate.now());
        promotion1.setEndDate(LocalDate.now().plusDays(30));
        promotion1.setPromotionType(PromotionType.DISCOUNT_PERCENTAGE);

        promotion2 = new Promotion();
        promotion2.setId(2L);
        promotion2.setName("Winter Sale");
        promotion2.setDescription("Discount on winter courses");
        promotion2.setNumberOfCourses(15);
        promotion2.setDiscountPercentage(BigDecimal.valueOf(15));
        promotion2.setDiscountAmount(BigDecimal.valueOf(100));
        promotion2.setExtraCourses(2);
        promotion2.setPayOnlyNumberCourses(13);
        promotion2.setStartDate(LocalDate.now());
        promotion2.setEndDate(LocalDate.now().plusDays(60));
        promotion2.setPromotionType(PromotionType.DISCOUNT_AMOUNT);

        promotionDTO1 = new PromotionDTO();
        promotionDTO1.setId(1L);
        promotionDTO1.setName("Summer Sale");
        promotionDTO1.setDescription("Discount on summer courses");
        promotionDTO1.setNumberOfCourses(10);
        promotionDTO1.setDiscountPercentage(BigDecimal.valueOf(10));
        promotionDTO1.setDiscountAmount(BigDecimal.valueOf(50));
        promotionDTO1.setExtraCourses(1);
        promotionDTO1.setPayOnlyNumberCourses(9);
        promotionDTO1.setStartDate(LocalDate.now());
        promotionDTO1.setEndDate(LocalDate.now().plusDays(30));
        promotionDTO1.setPromotionType(PromotionType.DISCOUNT_PERCENTAGE);

        promotionDTO2 = new PromotionDTO();
        promotionDTO2.setId(2L);
        promotionDTO2.setName("Winter Sale");
        promotionDTO2.setDescription("Discount on winter courses");
        promotionDTO2.setNumberOfCourses(15);
        promotionDTO2.setDiscountPercentage(BigDecimal.valueOf(15));
        promotionDTO2.setDiscountAmount(BigDecimal.valueOf(100));
        promotionDTO2.setExtraCourses(2);
        promotionDTO2.setPayOnlyNumberCourses(13);
        promotionDTO2.setStartDate(LocalDate.now());
        promotionDTO2.setEndDate(LocalDate.now().plusDays(60));
        promotionDTO2.setPromotionType(PromotionType.DISCOUNT_AMOUNT);
        
        
    }
    
    @Test
    void testSavePromotionSuccessfully() {
        // Mocking repository and mapper behavior
        when(promotionRepository.existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType())).thenReturn(0L);
        when(promotionMapper.toEntity(promotionDTO)).thenReturn(promotion);
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(promotionMapper.toDTO(promotion)).thenReturn(promotionDTO);

        // Calling the service method
        PromotionDTO savedPromotionDTO = promotionService.save(promotionDTO);

        // Assertions
        assertEquals(promotionDTO, savedPromotionDTO);

        // Verify interactions
        verify(promotionRepository, times(1)).existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType());
        verify(promotionRepository, times(1)).save(promotion);
        verify(promotionMapper, times(1)).toEntity(promotionDTO);
        verify(promotionMapper, times(1)).toDTO(promotion);
    }

    @Test
    void testSavePromotionThrowsExceptionWhenPromotionExists() {
        // Mocking repository behavior to return 1 when the promotion already exists
        when(promotionRepository.existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType())).thenReturn(1L);

        // Asserting the exception
        ApiException exception = assertThrows(ApiException.class, () -> {
            promotionService.save(promotionDTO);
        });

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Promotion is already exists", exception.getMessage());

        // Verify that the save method was never called
        verify(promotionRepository, times(1)).existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType());
        verify(promotionRepository, times(0)).save(any(Promotion.class));
        verify(promotionMapper, times(0)).toEntity(promotionDTO);
        verify(promotionMapper, times(0)).toDTO(any(Promotion.class));
    }
    
    @Test
    void testGetByIdSuccessfully() {
        // Mocking repository behavior to return a promotion when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        // Calling the service method
        Promotion foundPromotion = promotionService.getById(1L);

        // Assertions
        assertNotNull(foundPromotion);
        assertEquals(promotion.getId(), foundPromotion.getId());
        assertEquals(promotion.getName(), foundPromotion.getName());
        // Add more assertions as needed

        // Verify interactions
        verify(promotionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdThrowsExceptionWhenNotFound() {
        // Mocking repository behavior to return empty when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        // Asserting the exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            promotionService.getById(1L);
        });

        // Assertions
        assertEquals("Promotion", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // Verify interactions
        verify(promotionRepository, times(1)).findById(1L);
    }
    
    @Test
    void testUpdateSuccessfully() {
        // Mocking repository behavior to return 0 (not existing) when checking if promotion exists
        when(promotionRepository.existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType())).thenReturn(0L);

        // Mocking repository behavior to return an existing promotion when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        // Mocking the mapper to update the promotion
        doNothing().when(promotionMapper).updatePromotionFromDTO(promotionDTO, promotion);

        // Mocking the repository to save the promotion and return the updated promotion
        when(promotionRepository.save(promotion)).thenReturn(promotion);

        // Mocking the mapper to return promotionDTO when converting promotion to DTO
        when(promotionMapper.toDTO(promotion)).thenReturn(promotionDTO);

        // Calling the service method
        PromotionDTO updatedPromotionDTO = promotionService.update(1L, promotionDTO);

        // Assertions
        assertNotNull(updatedPromotionDTO);
        assertEquals(promotionDTO.getId(), updatedPromotionDTO.getId());
        assertEquals(promotionDTO.getName(), updatedPromotionDTO.getName());
        // Add more assertions as needed

        // Verify interactions
        verify(promotionRepository, times(1)).existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType());
        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionMapper, times(1)).updatePromotionFromDTO(promotionDTO, promotion);
        verify(promotionRepository, times(1)).save(promotion);
        verify(promotionMapper, times(1)).toDTO(promotion);
    }

    @Test
    void testUpdateThrowsApiExceptionWhenPromotionExists() {
        // Mocking repository behavior to return 1 (existing) when checking if promotion exists
        when(promotionRepository.existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType())).thenReturn(1L);

        // Asserting the exception
        ApiException exception = assertThrows(ApiException.class, () -> {
            promotionService.update(1L, promotionDTO);
        });

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Promotion is already exists", exception.getMessage());

        // Verify interactions
        verify(promotionRepository, times(1)).existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType());
        verify(promotionRepository, times(0)).findById(anyLong());
        verify(promotionMapper, times(0)).updatePromotionFromDTO(any(PromotionDTO.class), any(Promotion.class));
        verify(promotionRepository, times(0)).save(any(Promotion.class));
        verify(promotionMapper, times(0)).toDTO(any(Promotion.class));
    }

    @Test
    void testUpdateThrowsResourceNotFoundExceptionWhenPromotionNotFound() {
        // Mocking repository behavior to return 0 (not existing) when checking if promotion exists
        when(promotionRepository.existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType())).thenReturn(0L);

        // Mocking repository behavior to return empty when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        // Asserting the exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            promotionService.update(1L, promotionDTO);
        });

        // Assertions
        assertEquals("Promotion", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // Verify interactions
        verify(promotionRepository, times(1)).existsPromotion(promotionDTO.getNumberOfCourses(), promotionDTO.getPromotionType());
        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionMapper, times(0)).updatePromotionFromDTO(any(PromotionDTO.class), any(Promotion.class));
        verify(promotionRepository, times(0)).save(any(Promotion.class));
        verify(promotionMapper, times(0)).toDTO(any(Promotion.class));
    }
    
    
    @Test
    void testDeleteSuccessfully() {
        // Mocking repository behavior to return a promotion when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        // Calling the service method
        promotionService.delete(1L);

        // Verify interactions
        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionRepository, times(1)).delete(promotion);
    }

    @Test
    void testDeleteThrowsResourceNotFoundExceptionWhenPromotionNotFound() {
        // Mocking repository behavior to return empty when findById is called
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        // Asserting the exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            promotionService.delete(1L);
        });

        // Assertions
        assertEquals("Promotion", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // Verify interactions
        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionRepository, times(0)).delete(any(Promotion.class));
    }
    
    @Test
    void testGetPromotions() {
        // Mocking repository behavior to return a list of promotions
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion1, promotion2));
        when(promotionMapper.toDTO(promotion1)).thenReturn(promotionDTO1);
        when(promotionMapper.toDTO(promotion2)).thenReturn(promotionDTO2);

        // Calling the service method
        List<PromotionDTO> promotionDTOs = promotionService.getPromotions();

        // Assertions
        assertNotNull(promotionDTOs);
        assertEquals(2, promotionDTOs.size());
        assertEquals(promotionDTO1, promotionDTOs.get(0));
        assertEquals(promotionDTO2, promotionDTOs.get(1));

        // Verify interactions with the repository and mapper
        verify(promotionRepository, times(1)).findAll();
        verify(promotionMapper, times(1)).toDTO(promotion1);
        verify(promotionMapper, times(1)).toDTO(promotion2);
    }
}

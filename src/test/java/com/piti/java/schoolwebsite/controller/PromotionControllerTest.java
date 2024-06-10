package com.piti.java.schoolwebsite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.PromotionDTO;
import com.piti.java.schoolwebsite.enums.PromotionType;
import com.piti.java.schoolwebsite.mapper.PromotionMapper;
import com.piti.java.schoolwebsite.model.Promotion;
import com.piti.java.schoolwebsite.service.PromotionService;

@WebMvcTest(PromotionController.class)
public class PromotionControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromotionService promotionService;

    @MockBean
    private PromotionMapper promotionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Promotion promotion;
    private PromotionDTO promotionDTO;

    @BeforeEach
    void setUp() {
		promotion = new Promotion();
		promotion.setId(1L);
		promotion.setName("Summer Sale");
		promotion.setDescription("Discount on summer courses");
		promotion.setNumberOfCourses(10);
		promotion.setDiscountPercentage(BigDecimal.valueOf(10));
		promotion.setDiscountAmount(BigDecimal.valueOf(50));
		promotion.setExtraCourses(1);
		promotion.setPayOnlyNumberCourses(9);
		promotion.setStartDate(LocalDate.now());
		promotion.setEndDate(LocalDate.now().plusDays(30));
		promotion.setPromotionType(PromotionType.DISCOUNT_PERCENTAGE);
    	
    	
        promotionDTO = new PromotionDTO();
        promotionDTO.setId(1L);
        promotionDTO.setName("Summer Sale");
        promotionDTO.setDescription("Discount on summer courses");
        promotionDTO.setNumberOfCourses(10);
        promotionDTO.setDiscountPercentage(BigDecimal.valueOf(10));
        promotionDTO.setDiscountAmount(BigDecimal.valueOf(50));
        promotionDTO.setExtraCourses(1);
        promotionDTO.setPayOnlyNumberCourses(9);
        promotionDTO.setStartDate(LocalDate.now());
        promotionDTO.setEndDate(LocalDate.now().plusDays(30));
        promotionDTO.setPromotionType(PromotionType.DISCOUNT_PERCENTAGE);
    }

    @Test
    void testCreatePromotion() throws Exception {
        // Mock the behavior of the service
        when(promotionService.save(any(PromotionDTO.class))).thenReturn(promotionDTO);

        // Perform the POST request
        mockMvc.perform(post("/api/v1/promotion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promotionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(promotionDTO)));
    }
    
    @Test
    void testGetByIdSuccessfully() throws Exception {
        // Mock the behavior of the service and mapper
        when(promotionService.getById(anyLong())).thenReturn(promotion);
        when(promotionMapper.toDTO(promotion)).thenReturn(promotionDTO);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/promotion/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(promotionDTO)));
    }
    
    @Test
    void testUpdateSuccessfully() throws Exception {
        // Mock the behavior of the service and mapper
        when(promotionService.update(anyLong(), any(PromotionDTO.class))).thenReturn(promotionDTO);

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/promotion/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promotionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(promotionDTO)));
    }
    
    @Test
    void testDeleteByIdSuccessfully() throws Exception {
        // Mock the behavior of the service
        doNothing().when(promotionService).delete(anyLong());

        // Perform the DELETE request
        mockMvc.perform(delete("/api/v1/promotion/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void testGetAllSuccessfully() throws Exception {
        // Create a list of PromotionDTO objects to return
        PromotionDTO promotionDTO1 = new PromotionDTO();
        promotionDTO1.setId(1L);
        promotionDTO1.setName("Promotion 1");
        // set other fields for promotionDTO1

        PromotionDTO promotionDTO2 = new PromotionDTO();
        promotionDTO2.setId(2L);
        promotionDTO2.setName("Promotion 2");
        // set other fields for promotionDTO2

        List<PromotionDTO> promotionList = Arrays.asList(promotionDTO1, promotionDTO2);

        // Mock the service method
        when(promotionService.getPromotions()).thenReturn(promotionList);

        // Perform the GET request and expect a 200 OK status
        mockMvc.perform(get("/api/v1/promotion")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(promotionList)))
                .andExpect(jsonPath("$.length()").value(promotionList.size()))
                .andExpect(jsonPath("$[0].id").value(promotionDTO1.getId()))
                .andExpect(jsonPath("$[0].name").value(promotionDTO1.getName()))
                .andExpect(jsonPath("$[1].id").value(promotionDTO2.getId()))
                .andExpect(jsonPath("$[1].name").value(promotionDTO2.getName()));
    }
}

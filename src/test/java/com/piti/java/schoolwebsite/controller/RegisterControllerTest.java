package com.piti.java.schoolwebsite.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize; // Import Hamcrest's hasSize
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.CourseRegisterDTO;
import com.piti.java.schoolwebsite.dto.PageDTO;
import com.piti.java.schoolwebsite.dto.RegisterDTO;
import com.piti.java.schoolwebsite.mapper.PageMapper;
import com.piti.java.schoolwebsite.mapper.RegisterMapper;
import com.piti.java.schoolwebsite.service.RegisterService;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    @MockBean
    private RegisterMapper registerMapper;

    @MockBean
    private PageMapper pageMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterCourse() throws Exception {
        // Prepare test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUserId(1L);

        // Create and add CourseRegisterDTO objects
        CourseRegisterDTO course1 = new CourseRegisterDTO(1L);
        CourseRegisterDTO course2 = new CourseRegisterDTO(2L);
        registerDTO.setCourses(Arrays.asList(course1, course2));

        // Mock behavior of registerService
        doNothing().when(registerService).registerCourse(registerDTO);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
    /*
    @Test
    void testGetRegisterWithPayments() throws Exception {
        // Prepare test data
        Long registerId = 1L;
        User user = new User();
        user.setId(1L);

        Register register = new Register();
        register.setId(registerId);
        register.setUser(user);

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setId(registerId);
        registerDTO.setUserId(1L);

        // Mock behavior of registerService and registerMapper
        when(registerService.getRegisterIdWithPayments(anyLong())).thenReturn(register);
        when(registerMapper.toDTO(register)).thenReturn(registerDTO);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/v1/register/{id}", registerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(registerId))
                .andExpect(jsonPath("$.userId").value(1L));
    }
    */
    
    @Test
    void testGetRegistersList() throws Exception {
        // Prepare test data
        Map<String, String> params = new HashMap<>();
        // Add any required parameters to the map if needed

        Page<RegisterDTO> registersPage = new PageImpl<>(Collections.emptyList()); // Provide a mock Page object
        PageDTO pageDTO = new PageDTO(); // Mock PageDTO object

        // Mock behavior of registerService and pageMapper
        when(registerService.getRegisters(any())).thenReturn(registersPage);
        when(pageMapper.toDTO(registersPage)).thenReturn(pageDTO);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/v1/register").param("page", "1").param("size", "10").params(new LinkedMultiValueMap<String, String>()))
                .andExpect(status().isOk());
                
    }
    
    @Test
    void testGetRegistrationsByUser() throws Exception {
        // Prepare test data
        Long userId = 1L;
        List<RegisterDTO> registrations = Arrays.asList(
            // Create RegisterDTO objects as needed for the test
        );

        // Mock behavior of registerService
        when(registerService.getRegistrationsByUser(userId)).thenReturn(registrations);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/v1/register/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(registrations.size())));
            
    }
}

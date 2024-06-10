package com.piti.java.schoolwebsite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piti.java.schoolwebsite.dto.UserDTO;
import com.piti.java.schoolwebsite.enums.Role;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.model.User;
import com.piti.java.schoolwebsite.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
	 @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.EMPLOYEE);

        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setId(1L);
        createdUserDTO.setName("John Doe");
        createdUserDTO.setEmail("john@example.com");
        createdUserDTO.setPassword("password");
        createdUserDTO.setRole(Role.EMPLOYEE);

        when(userService.register(any(UserDTO.class))).thenReturn(createdUserDTO);

        // When / Then
        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }
    
    @Test
    public void testGetUserById_UserExists() throws Exception {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(Role.EMPLOYEE);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.EMPLOYEE);

        when(userService.getUserById(userId)).thenReturn(user);

        // When / Then
        mockMvc.perform(get("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    @Test
    public void testGetUserById_UserNotFound() throws Exception {
        // Given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User", "id", userId, HttpStatus.NOT_FOUND));

        // When / Then
        mockMvc.perform(get("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

package com.piti.java.schoolwebsite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piti.java.schoolwebsite.dto.UserDTO;
import com.piti.java.schoolwebsite.enums.Role;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.model.User;
import com.piti.java.schoolwebsite.repository.UserRepository;
import com.piti.java.schoolwebsite.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegister() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.EMPLOYEE);

        User user = new User();
        user.setId(1L);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserDTO savedUserDTO = userService.register(userDTO);

        // Then
        assertEquals(userDTO.getName(), savedUserDTO.getName());
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        assertEquals(userDTO.getRole(), savedUserDTO.getRole());
        // Add more assertions as needed
    }
    
    @Test
    public void testGetUserById_UserExists() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(Role.EMPLOYEE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User retrievedUser = userService.getUserById(userId);

        // Then
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getId());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getRole(), retrievedUser.getRole());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("User", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(userId, exception.getFieldValue());
    }
}

package com.piti.java.schoolwebsite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piti.java.schoolwebsite.dto.UserDTO;
import com.piti.java.schoolwebsite.mapper.UserMapper;
import com.piti.java.schoolwebsite.model.User;
import com.piti.java.schoolwebsite.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
	private UserService userService;
	
    @PostMapping
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.register(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}

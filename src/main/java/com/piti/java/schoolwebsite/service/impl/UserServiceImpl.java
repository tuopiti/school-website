package com.piti.java.schoolwebsite.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.piti.java.schoolwebsite.dto.UserDTO;
import com.piti.java.schoolwebsite.exception.ResourceNotFoundException;
import com.piti.java.schoolwebsite.mapper.UserMapper;
import com.piti.java.schoolwebsite.model.User;
import com.piti.java.schoolwebsite.repository.UserRepository;
import com.piti.java.schoolwebsite.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	
	@Override
	public UserDTO register(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toUser(userDTO);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDTO(savedUser);
    }

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id, HttpStatus.NOT_FOUND));
	}

}

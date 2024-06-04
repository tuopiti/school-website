package com.piti.java.schoolwebsite.service;

import com.piti.java.schoolwebsite.dto.UserDTO;
import com.piti.java.schoolwebsite.model.User;

public interface UserService {
	UserDTO register(UserDTO userDTO);
	User getUserById(Long id);
}

package com.piti.java.schoolwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piti.java.schoolwebsite.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}

package com.piti.java.schoolwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.piti.java.schoolwebsite.model.Register;

public interface RegisterRepository extends JpaRepository<Register, Long>{
	 @Query("SELECT r FROM Register r WHERE r.user.id = :userId")
	 List<Register> findByUserId(@Param("userId") Long userId);
}

package com.abhay.springproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhay.springproject.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByEmail(String email);
	User findByUsername(String email);

	
}

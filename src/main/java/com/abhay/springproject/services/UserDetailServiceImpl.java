package com.abhay.springproject.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.abhay.springproject.Entity.User;
import com.abhay.springproject.repository.UserRepository;
import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.entity.UserInfo;

public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
//		User user = userRepository.findByUsername(username);
//
//		if (user == null) {
//			throw new UsernameNotFoundException("UserNotFound");
//		}
//
//		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
//				user.getRole());
		User user = userRepository.findByUsername(username);
	        return user.map(UserInfoUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

		
	}

}

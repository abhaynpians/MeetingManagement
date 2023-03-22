package com.abhay.springproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.AuthRequest;
import com.abhay.springproject.dto.ListReq;
import com.abhay.springproject.repository.UserRepository;
import com.abhay.springproject.services.JwtService;
import com.abhay.springproject.services.SecurityService;

@RestController
public class HomeController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SecurityService securityService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;

	@Autowired
	BCryptPasswordEncoder bcrypt;

	@PostMapping("/login")
	@ResponseBody
	public String login(@RequestBody AuthRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(request.getEmail());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

	@PostMapping("/adduser")
	@ResponseBody
	public String addUser(@RequestBody User user) {

		System.out.println("Inside addUser");
		user.setStatus(1);
		user.setPassword(bcrypt.encode(user.getPassword()));
		userRepository.save(user);

		return "User Registered";
	}

	@GetMapping("/listuser")
	@ResponseBody
	public List<User> ListUsers(@RequestBody ListReq listReq) {

		if (listReq.getEmail() != null && listReq.getPhone() != 0) {
			return userRepository.findByEmail(listReq.getEmail());
		}

		return userRepository.findAll();
	}

	@GetMapping("/deactivateuser")
	@ResponseBody
	public String deactivateUser(@RequestBody int id) {
		User user = userRepository.findById(id).orElse(null);
		user.setStatus(0);
		return "User Deactivated ";
	}
}

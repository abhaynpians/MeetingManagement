package com.abhay.springproject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.Member;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.AuthRequest;
import com.abhay.springproject.dto.DeactReq;
import com.abhay.springproject.dto.ListReq;
import com.abhay.springproject.repository.MeetingRepository;
import com.abhay.springproject.repository.UserRepository;
import com.abhay.springproject.services.JwtService;


@RestController
@RequestMapping("/admin")
public class HomeController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MeetingRepository meetingRepository;
//	@Autowired
//	SecurityService securityService;

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
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String addUser(@RequestBody User user) {

		System.out.println("Inside addUser");
		user.setStatus(1);
		user.setPassword(bcrypt.encode(user.getPassword()));
		userRepository.save(user);

		return "User Registered";
	}

	@GetMapping("/listuser")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseBody
	public List<String> ListUsers(@RequestBody ListReq listReq) {

		if (listReq.getEmail() != null && listReq.getPhone() != 0) {
			//return userRepository.findByEmail(listReq.getEmail());
			
			Optional<User> data = userRepository.findByEmail(listReq.getEmail());
			List<String> resp = new ArrayList<>();
			resp.add(data.toString());
			return resp;
			
		}
		return null;

//		return userRepository.findAll();
	}

	@GetMapping("/deactivateuser")
//	@PreAuthorize("hasAuthority('ADMIN')")
	@PreAuthorize("hasRole('ADMIN')")
	public String deactivateUser(@RequestBody DeactReq req) {
		System.out.println("Inside Deacivate");
		User user = userRepository.findById(req.getId()).orElse(null);
		user.setStatus(0);
		userRepository.save(user);
		System.out.println("Inside Deacivate");
		return "User Deactivated ";
	}
	
	
	@PostMapping("/createMeeting")
	@PreAuthorize("hasRole('ADMIN','USER')")
	@ResponseBody
	public Meeting createmeeting(@RequestBody Meeting request) {
		List<Member> mem=request.getMembers();
		for(Member val:mem) {
			val.setMeeting(request);
			
		}
		request.setMembers(mem);
	   return  meetingRepository.save(request);
	}
	
	
	
	
	
}

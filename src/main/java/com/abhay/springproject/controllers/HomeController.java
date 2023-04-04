package com.abhay.springproject.controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.MeetingListReq;
import com.abhay.springproject.Entity.Member;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.AuthRequest;
import com.abhay.springproject.dto.CancelMeetingReq;
import com.abhay.springproject.dto.ChangePass;
import com.abhay.springproject.dto.DeactReq;
import com.abhay.springproject.dto.ListReq;
import com.abhay.springproject.dto.MeetingList;
import com.abhay.springproject.dto.TokenResponse;
import com.abhay.springproject.filter.JwtAuthFilter;
import com.abhay.springproject.repository.MeetingRepository;
import com.abhay.springproject.repository.UserRepository;
import com.abhay.springproject.services.JwtService;
import com.abhay.springproject.services.MeetingService;

@RestController
public class HomeController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MeetingRepository meetingRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;
	
//	@Autowired
//	TokenResponse response;
	
	@Autowired
	 JwtAuthFilter filter;
	
	@Autowired
	MeetingService meetingService;

	@Autowired
	BCryptPasswordEncoder bcrypt;

	@PostMapping("/admin/login")
	@ResponseBody
	public TokenResponse login(@RequestBody AuthRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		if (authentication.isAuthenticated()) {
			String token= jwtService.generateToken(request.getEmail());
			User user = userRepository.getDataByMail(request.getEmail());
			return new TokenResponse(token,user.getName(),user.getRole());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

	@PostMapping("/admin/changePassword")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String changePassword(@RequestBody ChangePass request) {
		System.out.println("inside Change Pass");
		User user = userRepository.findById(request.getId()).orElse(null);
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), request.getOldPassword()));

		System.out.println("Inside Change Pass");
		if (authentication.isAuthenticated()) {
			user.setPassword(bcrypt.encode(request.getNewPassword()));
			userRepository.save(user);
			return "Password Changed";
		}
		return "Password Not Changed";
	}

	
	@PostMapping("/admin/adduser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String addUser(@RequestBody User user) {

		System.out.println("Inside addUser");
		user.setStatus(1);
		user.setPassword(bcrypt.encode(user.getPassword()));
		userRepository.save(user);

		return "User Registered";
	}

	@GetMapping("/admin/listuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public List<User> ListUsers(@RequestBody ListReq listReq) {

		if (listReq.getEmail() != null) {
			// return userRepository.findByEmail(listReq.getEmail());

			List<User> data = userRepository.getData(listReq.getEmail());
			return data;

		} else if (listReq.getPhone() != 0) {
			List<User> data = userRepository.getDataByPhone(listReq.getPhone());
			return data;

		}

		List<User> data = userRepository.findAll();
		return data;

	}

	@GetMapping("/admin/deactivateuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String deactivateUser(@RequestBody DeactReq req) {
		System.out.println("Inside Deacivate");
		User user = userRepository.findById(req.getId()).orElse(null);
		user.setStatus(0);
		userRepository.save(user);
		System.out.println("Inside Deacivate");
		return "User Deactivated ";
	}

	@PostMapping("/admin/createMeeting")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String createmeeting(@RequestBody Meeting request) {
		System.out.println("Inside Create Meeting");
		
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		dateFormate.setTimeZone(TimeZone.getTimeZone("UTC-5:30"));
		/*
		 * java.util.Date date; java.util.Date endDate;
		 * 
		 * String stringDate = request.getStart(); try { date =
		 * inputFormat.parse(request.getStart()); endDate=
		 * inputFormat.parse(request.getEnd()); String start = dateFormate.format(date);
		 * String end = dateFormate.format(endDate);
		 * 
		 * request.setStart(start); request.setEnd(end);
		 * 
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		 
		
		String username=meetingService.validateOrganiser(filter.getToken());
		User user = userRepository.getDataByMail(username);
		String name = user.getName();
		request.setOrganiser(name);
		request.setOrganiserId(user.getId());
		boolean timevalidation=meetingService.validateMeetingTime(request.getStart(), request.getEnd(), user.getId());
		if(!timevalidation) {
			return "You can not create the Meeting in same time";
		}
		

		
		
		List<Member> mem = request.getMembers();
		for (Member val : mem) {
			val.setMeeting(request);

		}
		request.setMembers(mem); 
		
		meetingRepository.save(request);
		return "Meeting Created";

	}

	@PostMapping("/admin/meetingList")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public List<MeetingListReq> meetingList(@RequestBody MeetingList request) {
		System.out.println("Inside List");
		return meetingRepository.getMeetingList(request.getStart(), request.getEnd());

	}

	@PostMapping("/admin/cancelMeeting")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseBody
	public String cancelMeeting(@RequestBody CancelMeetingReq request) {
		Meeting data = meetingRepository.findById(request.getId()).orElseGet(null);
		String organiserByDefault = data.getOrganiser();
		String organiser = request.getOrganiser();
		if (organiser.equals(organiserByDefault)) {
			meetingRepository.deleteById(data.getId());
			return "Meeting Deleted";
		}
		return "Something went Wrong";

	}

}

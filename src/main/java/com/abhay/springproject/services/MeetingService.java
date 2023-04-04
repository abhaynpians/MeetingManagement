package com.abhay.springproject.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abhay.springproject.Entity.Meeting;

import com.abhay.springproject.repository.MeetingRepository;
import com.abhay.springproject.repository.UserRepository;

@Component
public class MeetingService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	MeetingRepository meetingRepository;
	
	
	
	
	public String validateOrganiser(String token) {
		
		String username= jwtService.extractUsername(token);
		return username;
		
	}
	
	public boolean validateMeetingTime(Timestamp start , Timestamp end, int id ) {
		
		
		List<Meeting> listOfMeeting = meetingRepository.validateMeetingTime(start,end,id);
		
		
		
		//Meeting data = meetingRepository.validateMeeting(start,id);
		if(!listOfMeeting.isEmpty()) {
			return false;
		}
		else {
			return true;
		}
		
	}
	/* public String validateMember(String) */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

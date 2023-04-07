package com.abhay.springproject.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.Member;
import com.abhay.springproject.Entity.User;
import com.abhay.springproject.dto.StartMeeting;
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
	
	public boolean startMeeting(StartMeeting request) {
		Meeting meeting=meetingRepository.findById(request.getMeetingId()).orElse(null);
		meeting.setMeetingstatus("Started");
		List<Member> member = meeting.getMembers();
		/*
		 * for (Member member2 : member) { //Send Notification that meeting is started }
		 */
		User organiser = userRepository.findById(request.getOrganiserId()).orElse(null);
		organiser.setAvailability("InMeeting");
		
		return true;
		
		
	}
	
	
//	public boolean validateMember()
	
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

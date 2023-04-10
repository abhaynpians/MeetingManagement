package com.abhay.springproject.dto;

import lombok.Data;

@Data
public class JoinMeeting {
	private int meetingId;
	private int memberId;
	private String memberName;

}

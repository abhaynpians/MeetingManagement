package com.abhay.springproject.dto;

import com.abhay.springproject.Entity.Meeting;

public class MeetingRequest {

	private Meeting meeting;

	@Override
	public String toString() {
		return "MeetingRequest [meeting=" + meeting + "]";
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
}

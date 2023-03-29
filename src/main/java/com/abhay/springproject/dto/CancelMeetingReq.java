package com.abhay.springproject.dto;

public class CancelMeetingReq {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrganiser() {
		return organiser;
	}
	public void setOrganiser(String organiser) {
		this.organiser = organiser;
	}
	private String organiser;
	

}

package com.abhay.springproject.dto;

import java.util.Date;

public class MeetingList {
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	private Date start;
	private Date end;

}

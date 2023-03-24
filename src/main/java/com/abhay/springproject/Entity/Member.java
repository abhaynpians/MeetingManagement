package com.abhay.springproject.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(   generator = ObjectIdGenerators.PropertyGenerator.class,
property = "mid")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mid;
	private String mname;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mm_fk")
	@JsonIdentityReference(alwaysAsId = true)
	private Meeting meeting;

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Member() {
		super();
	}

	public Member(int mid, String mname, Meeting meeting) {
		super();
		this.mid = mid;
		this.mname = mname;
		this.meeting = meeting;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	@Override
	public String toString() {
		return "Member [mid=" + mid + ", mname=" + mname + "]";
	}

}


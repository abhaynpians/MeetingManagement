package com.abhay.springproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.Member;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

	public List<Member> findMembersById(int id);
}

package com.abhay.springproject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.MeetingListReq;
import com.abhay.springproject.Entity.Member;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

	public List<Member> findMembersById(int id);

	@Query(nativeQuery = true)
	List<MeetingListReq> getMeetingList(@Param("start") Date start, @Param("end") Date end);

}

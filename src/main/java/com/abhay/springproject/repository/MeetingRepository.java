package com.abhay.springproject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abhay.springproject.Entity.Meeting;
import com.abhay.springproject.Entity.Member;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

	public List<Member> findMembersById(int id);
	//public static final String hello="select id , link from meeting where meeting.start=:start and meeting.end=:end";
	
	@Query(value="select link, title from meeting",nativeQuery = true)
	//@Query(value="select * from meeting m where m.start=:start and m.end=:end",nativeQuery = true)
//	@Query(value=hello,nativeQuery = true)
	List<Meeting> getList(@Param("start") Date start,@Param("end") Date end);
}

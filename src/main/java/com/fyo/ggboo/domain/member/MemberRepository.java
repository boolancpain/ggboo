package com.fyo.ggboo.domain.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Member Repository
 * 
 * @author boolancpain
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
	/**
	 * 회원 id로 검색
	 * 
	 * @param memberId
	 */
	public Optional<Member> findByMemberId(String memberId);
	
	/**
	 * 회원 id로 중복검사
	 * 
	 * @param memberId
	 */
	public boolean existsByMemberId(String memberId);
	
	/**
	 * 회원 목록 조회
	 */
	public Page<Member> findAll(Pageable pageable);
}
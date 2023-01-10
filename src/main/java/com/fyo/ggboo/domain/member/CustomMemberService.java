package com.fyo.ggboo.domain.member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fyo.ggboo.global.enums.MemberRole;

/**
 * UserDetailsService를 구현한 서비스
 * 
 * @author boolancpain
 */
@Service
public class CustomMemberService implements UserDetailsService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	/**
	 * UserDetailsService의 로그인 메소드
	 * 
	 * @param username(memberId)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByMemberId(username)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException(null));
	}
	
	/**
	 * Member Entity로 UserDetails 인스턴스 생성
	 * 
	 * @param member
	 */
	private UserDetails createUserDetails(Member member) {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		// user 권한 부여
		roles.add(new SimpleGrantedAuthority(MemberRole.ROLE_USER.toString()));
		
		// admin 계정에만 관리자 권한 부여
		if(member.getMemberId().equalsIgnoreCase("ADMIN")) {
			roles.add(new SimpleGrantedAuthority(MemberRole.ROLE_ADMIN.toString()));
		}
		
		return new User(String.valueOf(member.getId()), member.getPassword(), roles);
	}
}
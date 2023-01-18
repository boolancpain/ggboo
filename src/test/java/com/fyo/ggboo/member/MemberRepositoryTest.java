package com.fyo.ggboo.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.fyo.ggboo.domain.member.Member;
import com.fyo.ggboo.domain.member.MemberRepository;

/**
 * 회원 Repository 테스트
 * 
 * @author boolancpain
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
public class MemberRepositoryTest {
	
	@Autowired
	private MemberRepository memberRepository;
	
	private 
	
	@Test
	void 회원등록() {
		String testMemberId = "test0";
		String testMemberName = "테스트";
		String testPassword = "1234";
		
		Member member = Member.builder()
				.memberId(testMemberId)
				.memberName(testMemberName)
				.password(testPassword)
				.build();
		
		Member savedMember = memberRepository.save(member);
		System.out.println(savedMember.getId());
		
		assertThat(savedMember).isNotNull();
		assertThat(savedMember.getMemberId()).isEqualTo(testMemberId);
		assertThat(savedMember.getMemberName()).isEqualTo(testMemberName);
		assertThat(savedMember.getPassword()).isEqualTo(testPassword);
	}
}
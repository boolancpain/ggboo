package com.fyo.ggboo.domain.member;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.domain.member.MemberRequestDto.LoginDto;
import com.fyo.ggboo.domain.member.MemberRequestDto.MemberSignupDto;
import com.fyo.ggboo.domain.member.MemberRequestDto.MemberUpdateDto;
import com.fyo.ggboo.domain.member.MemberResponseDto.MemberInfoDto;
import com.fyo.ggboo.domain.member.MemberResponseDto.TokenDto;
import com.fyo.ggboo.global.SecurityUtil;
import com.fyo.ggboo.infra.jwt.TokenProvider;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * Member Service
 * 
 * @author boolancpain
 */
@Service
public class MemberService {
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CustomMessageSource customMessageSource;
	
	/**
	 * 로그인 처리
	 * 
	 * @param loginDto
	 */
	public ResponseEntity<?> login(LoginDto loginDto) {
		// 회원 존재 유무 체크
		if(!memberRepository.existsByMemberId(loginDto.getId())) {
			// 404
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(BaseResponse.builder()
							.message(customMessageSource.getMessage("member.notfound"))
							.build());
		}
		
		// Login id/pw를 기반으로 AuthenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
		
		// 회원 검증. authenticate 메소드가 실행될 때 CustomMemberService의 loadUserByUsername 메소드가 실행되며 회원 인증을 실행함.
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		// 인증 정보로 jwt 토큰 생성
		String jwt = tokenProvider.generateToken(authentication);
		
		// SecurityContext에 인증 객체 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// 200
		return ResponseEntity.ok(TokenDto.builder().token(jwt).build());
	}
	
	/**
	 * 회원 가입
	 * 
	 * @param signupDto
	 */
	public ResponseEntity<?> signup(MemberSignupDto memberSignupDto) {
		// 회원 중복 검사
		if(memberRepository.existsByMemberId(memberSignupDto.getMemberId())) {
			// 409
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(BaseResponse.builder()
							.message(customMessageSource.getMessage("member.exists.memberid"))
							.build());
		}
		
		Member member = Member.builder()
				.memberId(memberSignupDto.getMemberId().toLowerCase())
				.memberName(memberSignupDto.getMemberName())
				.password(passwordEncoder.encode(memberSignupDto.getPassword()))
				.build();
		
		// save
		memberRepository.save(member);
		
		// 201
		return ResponseEntity.created(null).build();
	}
	
	/**
	 * 로그인 인증된 회원 정보 조회
	 * 
	 * @throws Exception 
	 */
	public ResponseEntity<?> getMyInfo() {
		Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
				.orElseThrow(() -> new UsernameNotFoundException(null));
		
		MemberInfoDto memberInfo = MemberInfoDto.builder()
				.id(member.getId())
				.memberId(member.getMemberId())
				.memberName(member.getMemberName())
				.build();
		
		// 200
		return ResponseEntity.ok(memberInfo);
	}
	
	/**
	 * 로그인 인증된 회원 정보 수정
	 * 
	 * @param memberUpdateDto
	 */
	@Transactional
	public ResponseEntity<?> updateMyInfo(MemberUpdateDto memberUpdateDto) {
		// 회원 정보 수정
		return this.updateMember(SecurityUtil.getCurrentMemberId(), memberUpdateDto);
	}
	
	/**
	 * 회원 id로 회원 정보를 조회
	 * 
	 * @param memberId
	 */
	public ResponseEntity<?> getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("member.notfound")));
		
		MemberInfoDto memberInfo = MemberInfoDto.builder()
				.id(member.getId())
				.memberId(member.getMemberId())
				.memberName(member.getMemberName())
				.build();
		
		// 200
		return ResponseEntity.ok(memberInfo);
	}
	
	/**
	 * 회원 목록 조회
	 * 
	 * @param pageable
	 */
	public ResponseEntity<?> getMemberList(Pageable pageable) {
		Page<Member> memberList = memberRepository.findAll(pageable);
		
		List<MemberInfoDto> memberDtoList = memberList.stream()
				.map(member -> MemberInfoDto.builder()
						.id(member.getId())
						.memberId(member.getMemberId())
						.memberName(member.getMemberName())
						.build())
				.collect(Collectors.toList());
		
		// 200
		return ResponseEntity.ok(memberDtoList);
	}
	
	/**
	 * 회원 삭제
	 * 
	 * @param memberId
	 */
	public ResponseEntity<?> deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("member.notfound")));
		
		// 삭제
		memberRepository.delete(member);
		
		// 204
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * 회원 정보 수정
	 * 
	 * @param memberId
	 * @param memberRequestDto
	 */
	@Transactional
	public ResponseEntity<?> updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("member.notfound")));
		
		// entity update
		member.updateMemberName(memberUpdateDto.getMemberName());
		
		MemberInfoDto memberInfo = MemberInfoDto.builder()
				.id(member.getId())
				.memberId(member.getMemberId())
				.memberName(member.getMemberName())
				.build();
		
		// 200
		return ResponseEntity.ok(memberInfo);
	}
}
package com.fyo.ggboo.domain.member;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fyo.ggboo.domain.member.MemberRequestDto.LoginDto;
import com.fyo.ggboo.domain.member.MemberRequestDto.MemberSignupDto;
import com.fyo.ggboo.domain.member.MemberRequestDto.MemberUpdateDto;

/**
 * Member Controller
 * 
 * @author boolancpain
 */
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	/**
	 * 로그인
	 * 
	 * @param loginDto
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
		return memberService.login(loginDto);
	}
	
	/**
	 * 회원가입
	 * 
	 * @param signupDto
	 */
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody MemberSignupDto memberSignupDto) {
		return memberService.signup(memberSignupDto);
	}
	
	/**
	 * 로그인 인증된 회원 정보 조회
	 */
	@GetMapping(value = "/me")
	public ResponseEntity<?> getMyInfo() {
		return memberService.getMyInfo();
	}
	
	/**
	 * 로그인 인증된 회원 정보 수정
	 * 
	 * @param memberUpdateDto
	 */
	@PutMapping(value = "/me")
	public ResponseEntity<?> updateMyInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) {
		return memberService.updateMyInfo(memberUpdateDto);
	}
	
	/**
	 * 회원 정보 조회
	 * 
	 * @param memberId
	 */
	@GetMapping(value = "/members/{memberId}")
	public ResponseEntity<?> getMember(@PathVariable Long memberId) {
		return memberService.getMember(memberId);
	}
	
	/**
	 * 회원 목록 조회
	 * 
	 * @param pageable
	 */
	@GetMapping(value = "/members")
	public ResponseEntity<?> getMemberList(@PageableDefault(page = 0, size = 10, sort="createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return memberService.getMemberList(pageable);
	}
	
	/**
	 * 회원 삭제
	 * 
	 * @param memberId
	 */
	@DeleteMapping(value = "/members/{memberId}")
	public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
		return memberService.deleteMember(memberId);
	}
	
	/**
	 * 회원 정보 수정
	 * 
	 * @param memberId
	 * @param memberRequestDto
	 */
	@PutMapping(value = "/members/{memberId}")
	public ResponseEntity<?> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateDto memberUpdateDto) {
		return memberService.updateMember(memberId, memberUpdateDto);
	}
}
package com.fyo.ggboo.domain.member;

import lombok.Builder;
import lombok.Getter;

/**
 * 회원 관련 Response Dto
 * 
 * @author boolancpain
 */
public class MemberResponseDto {
	
	/**
	 * 토큰 Dto
	 * 
	 * @author boolancpain
	 */
	@Getter
	static class TokenDto {
		
		private String token;
		
		@Builder
		public TokenDto(String token) {
			this.token = token;
		}
	}
	
	/**
	 * 회원 정보 Dto
	 * 
	 * @author boolancpain
	 */
	@Getter
	static class MemberInfoDto {
		
		private Long id;
		
		private String memberId;
		
		private String memberName;
		
		@Builder
		public MemberInfoDto(Long id, String memberId, String memberName) {
			this.id = id;
			this.memberId = memberId;
			this.memberName = memberName;
		}
	}
}
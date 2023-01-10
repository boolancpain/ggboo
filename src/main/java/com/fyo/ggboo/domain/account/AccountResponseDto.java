package com.fyo.ggboo.domain.account;

import lombok.Builder;
import lombok.Getter;

/**
 * 장부 관련 Response Dto
 * 
 * @author boolancpain
 */
public class AccountResponseDto {
	
	/**
	 * Account Info Dto
	 * 
	 * @author boolancpain
	 */
	@Getter
	static class AccountInfoDto {
		
		private Long accountId;
		
		@Builder
		public AccountInfoDto(Long accountId) {
			this.accountId = accountId;
		}
	}
}

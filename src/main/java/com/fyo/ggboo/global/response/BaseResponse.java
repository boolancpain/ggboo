package com.fyo.ggboo.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 기본 응답 객체
 * 
 * @author boolancpain
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseResponse {
	
	private String message;
	
	@Builder
	public BaseResponse(String message) {
		this.message = message;
	}
}
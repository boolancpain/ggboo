package com.fyo.ggboo.global;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

/**
 * 인증된 회원 정보 추출 class
 * 
 * @author boolancpain
 */
public class SecurityUtil {
	
	/**
	 * JwtFilter에서 저장한 회원 인증 객체에서 회원 id를 추출
	 */
	public static Long getCurrentMemberId() {
		// JwtFilter에서 저장한 Authentication 인증 객체를 Security Context로부터 가져옴
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || !StringUtils.hasText(authentication.getName())) {
			// 401
			throw new AuthenticationCredentialsNotFoundException("Not found authentication from security context!");
		}
		
		return Long.parseLong(authentication.getName());
	}
}
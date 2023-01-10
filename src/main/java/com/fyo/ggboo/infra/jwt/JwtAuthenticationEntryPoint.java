package com.fyo.ggboo.infra.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * AuthenticationEntryPoint 구현체
 * 
 * @author boolancpain
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Autowired
	private CustomMessageSource customMessageSource;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		// default message source key
		String key = "unauthorized";
		
		if(authException instanceof BadCredentialsException) {
			key = "member.incorrect";
		}
		
		// 유효한 자격증명을 제공하지 않고 접근할 때 401 에러
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(BaseResponse.builder()
				.message(customMessageSource.getMessage(key))
				.build()));
	}
}
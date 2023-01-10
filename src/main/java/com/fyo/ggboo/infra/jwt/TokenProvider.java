package com.fyo.ggboo.infra.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fyo.ggboo.infra.message.CustomMessageSource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 컴포넌트
 * 
 * @author boolancpain
 */
@Component
@Slf4j
public class TokenProvider {
	
	private final static String SECRET_KEY = "ZnlvLWdnYm9vLXNlcnZpY2UtZGV2ZWxvcGVkLWJ5LWJvb2xhbmNwYWluLXNpbmNlLTIwMjEK";
	
	private final static String AUTHORITIES_KEY = "auth";
	
	private final static long EXPIRED_TIME = 1000L * 60 * 60 * 24;	// 1일
	
	private final Key key;
	
	@Autowired
	private CustomMessageSource customMessageSource;
	
	public TokenProvider() {
		this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * 토큰 생성
	 * 
	 * @param authentication
	 */
	public String generateToken(Authentication authentication) {
		// 회원 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		return Jwts.builder()
			.setSubject(authentication.getName())	// 토큰 제목
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRED_TIME))	// 토큰 유효기간
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}
	
	/**
	 * 토큰의 유효성을 검증
	 * 
	 * @param token
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.debug(customMessageSource.getMessage("token.incorrect"));
		} catch (ExpiredJwtException e) {
			log.debug(customMessageSource.getMessage("token.expired"));
		} catch (UnsupportedJwtException e) {
			log.debug(customMessageSource.getMessage("token.unsupported"));
		} catch (IllegalArgumentException e) {
			log.debug(customMessageSource.getMessage("token.error"));
		}
		
		return false;
	}
	
	/**
	 * 토큰 정보로 Authentication 인증 객체를 생성
	 * 
	 * @param token
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		User principal = new User(claims.getSubject(), "", authorities);
		
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
}
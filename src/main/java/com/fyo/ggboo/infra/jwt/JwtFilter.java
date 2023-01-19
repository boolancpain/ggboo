package com.fyo.ggboo.infra.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jwt Filter
 * 
 * @author boolancpain
 */
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private List<String> excludeUrls = Arrays.asList(new String[] {"/login", "/signup"});

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        // 토큰 추출
        String token = this.extractToken(request);
        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 토큰으로 인증 객체 생성
            Authentication authentication = tokenProvider.getAuthentication(token);
            // Security Context에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. uri : {}", authentication.getName(), requestUri);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다. uri : {}", requestUri);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * http request header에서 토큰 추출
     * 
     * @param request
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * jwt 필터를 적용하지 않을 url 설정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrls.stream().anyMatch(url -> url.equalsIgnoreCase(request.getServletPath()));
    }
}
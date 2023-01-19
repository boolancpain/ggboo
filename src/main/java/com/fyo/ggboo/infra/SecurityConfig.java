package com.fyo.ggboo.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fyo.ggboo.infra.jwt.JwtAccessDeniedHandler;
import com.fyo.ggboo.infra.jwt.JwtAuthenticationEntryPoint;
import com.fyo.ggboo.infra.jwt.JwtSecurityConfig;
import com.fyo.ggboo.infra.jwt.TokenProvider;

/**
 * Security 설정
 * 
 * @author boolancpain
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 요청에는 Simple request와 Preflight request가 존재함.
     * 브라우저에서 Simple request에 해당하지 않으면 Preflight request로 요청하게 됨
     * 로그인 인증 후 header에 Authorization을 담아 보내기때문에 해당 설정을 통해 CORS 이슈를 피할 수 있음
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // csrf 사용 x
            .csrf().disable()

            // cors 허용 설정
            .cors().configurationSource(this.corsConfigurationSource())

            // Security는 기본으로 session을 사용하기 때문에 stateless로 변경
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API를 제외한 요청에 대해서는 인증이 필요하도록 설정
            // /members/ 하위 url은 관리자 권한 필요
            .and()
            .authorizeRequests()
            .antMatchers("/login", "/signup").permitAll()
            .antMatchers("/members/**").hasRole("ADMIN")
            .anyRequest().authenticated()

            // 예외처리 핸들링 할 클래스 지정
            .and()
            .exceptionHandling()
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)

            // Jwt Security 설정 적용
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));
    }
}
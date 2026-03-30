package com.telusko.spring_sec_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 스프링 설정 클래스
@Configuration
// 웹 보안 활성화
@EnableWebSecurity
public class SecurityConfig {

    // 사용자 정보 조회 서비스 주입
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    // 실제 인증 로직 담당 요원 설정
    @Bean

    public AuthenticationProvider authProvider() {
        // DB 기반 인증 제공자 생성
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 정보 조회 서비스 연결
        provider.setUserDetailsService(userDetailsService);
        // 암호 검증용 알고리즘 지정
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return provider;
    }

    // 보안 필터 체인 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(customizer -> customizer.disable())
                // URL 접근 권한 정의
                .authorizeHttpRequests(request -> request
                        // 회원가입 및 로그인 경로 개방
                        .requestMatchers("register", "login")
                        .permitAll()
                        // 나머지 요청 인증 필수
                        .anyRequest()
                        .authenticated())
                // HTTP 기본 인증 사용
                .httpBasic(Customizer.withDefaults())
                // 무상태성 세션 정책 적용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 총괄 관리자 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // 설정 정보 바탕 관리자 객체 반환
        return config.getAuthenticationManager();
    }
}
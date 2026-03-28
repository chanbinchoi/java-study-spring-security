package com.telusko.spring_sec_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 설정 클래스 지정
@Configuration
// 웹 보안 기능 활성화
@EnableWebSecurity
public class SecurityConfig {

    // DB에서 사용자 정보 가져오는 서비스 주입
    @Autowired
    private UserDetailsService userDetailsService;

    // 인증 처리 핵심 로직 정의
    @Bean
    public AuthenticationProvider authProvider() {
        // 데이터 접근용 인증 제공자 객체 생성
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 사용자 정보 조회 서비스 연결
        provider.setUserDetailsService(userDetailsService);
        // 암호 검증 방식 설정 (BCrypt 12단계 강도)
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return provider;
    }

    // 보안 필터 체인 규칙 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 방어 비활성화
                .csrf(customizer -> customizer.disable())
                // 모든 요청 인증 필수
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                // 기본 로그인 팝업 사용
                .httpBasic(Customizer.withDefaults())
                // 세션 생성 차단 (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /*
    // 사용자 명부 관리 메서드
    @Bean
    public UserDetailsService userDetailsService() {

        // 일반 사용자 정보 생성
        UserDetails user = User
                .withDefaultPasswordEncoder() // 기본 비밀번호 암호화 도구 사용
                .username("navin")           // 아이디 설정
                .password("n@123")           // 비밀번호 설정
                .roles("USER")               // 일반 사용자 등급 부여
                .build();

        // 관리자 정보 생성
        UserDetails admin = User
                .withDefaultPasswordEncoder() // 암호화 적용
                .username("admin")           // 아이디 설정
                .password("admin@789")       // 비밀번호 설정
                .roles("ADMIN")              // 관리자 등급 부여
                .build();

        // 메모리 기반 사용자 관리자 반환
        return new InMemoryUserDetailsManager(user, admin);
    }
     */
}
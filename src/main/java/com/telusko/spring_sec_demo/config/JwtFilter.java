package com.telusko.spring_sec_demo.config;

import com.telusko.spring_sec_demo.service.JwtService;
import com.telusko.spring_sec_demo.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 스프링 관리 컴포넌트 등록
@Component
// 요청당 1회 실행 보장 필터 상속
public class JwtFilter extends OncePerRequestFilter {

    // JWT 파싱 및 검증 서비스 주입
    @Autowired
    JwtService jwtService;

    // 스프링 애플리케이션 컨텍스트 주입
    @Autowired
    ApplicationContext context;

    // 핵심 필터 검증 로직
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // HTTP 요청 헤더 내 Authorization 값 추출
        String authHeader = request.getHeader("Authorization");

        // 토큰 저장 변수
        String token = null;

        // 사용자 이름 저장 변수
        String userName = null;

        // 토큰 존재 및 Bearer 타입 여부 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " 문자열(7글자) 제외 순수 토큰 추출
            token = authHeader.substring(7);
            // 토큰 내부 사용자 이름 추출
            userName = jwtService.extractUserName(token);
        }

        // 사용자 이름 존재 및 현재 미인증 상태 확인
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // DB 내 최신 사용자 정보 조회
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

            // 토큰 유효성 최종 검사
            if (jwtService.validateToken(token, userDetails)) {
                // 검증 완료 기반 인증 객체 생성 (권한 정보 포함)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 요청 기반 세부 정보(IP 등) 추가
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 스프링 보안 컨텍스트 내 인증 정보 저장 (로그인 처리)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 다음 필터 또는 컨트롤러로 요청 전달
        filterChain.doFilter(request, response);
    }
}
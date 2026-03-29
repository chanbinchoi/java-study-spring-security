package com.telusko.spring_sec_demo.controller;

import com.telusko.spring_sec_demo.model.User;
import com.telusko.spring_sec_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 데이터 반환 컨트롤러
@RestController
public class UserController {

    // 인증 총괄 관리자 주입
    @Autowired
    AuthenticationManager authenticationManager;

    // 회원 비즈니스 로직 서비스 주입
    @Autowired
    private UserService service;

    // 회원가입 요청 처리
    @PostMapping("register")
    public User register(@RequestBody User user) {
        // 사용자 정보 저장 및 결과 반환
        return service.saveUser(user);
    }

    // 로그인 요청 처리
    @PostMapping("login")
    public String login(@RequestBody User user) {
        // 사용자 입력 기반 인증용 토큰 생성
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // 인증 성공 여부 확인
        if (authentication.isAuthenticated())
            // 로그인 성공 메시지 반환
            return "Success";
        else
            // 로그인 실패 메시지 반환
            return "Login Failed";
    }
}
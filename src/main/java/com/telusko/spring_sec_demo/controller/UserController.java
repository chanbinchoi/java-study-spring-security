package com.telusko.spring_sec_demo.controller;

import com.telusko.spring_sec_demo.model.User;
import com.telusko.spring_sec_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 데이터 반환 전용 컨트롤러 선언
@RestController
public class UserController {

    // 회원 관리 로직 서비스 주입
    @Autowired
    private UserService service;

    // 회원가입 요청 처리 (POST 방식)
    @PostMapping("register")
    public User register(@RequestBody User user) {
        // 서비스 계층의 저장 로직 호출 및 결과 반환
        return service.saveUser(user);
    }
}
package com.telusko.spring_sec_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 데이터 반환용 컨트롤러 선언
@RestController
public class HelloController {

    // /hello 경로 접속 시 실행
    @GetMapping("hello")
    public String greet(HttpServletRequest request) {
        // 인사말과 고유 세션 ID 결합 반환
        return "Hello World " + request.getSession().getId();
    }

    // /about 경로 접속 시 실행
    @GetMapping("about")
    public String about(HttpServletRequest request) {
        // 이름과 고유 세션 ID 결합 반환
        return "Telusko " + request.getSession().getId();
    }
}
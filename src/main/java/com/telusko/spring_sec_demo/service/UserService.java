package com.telusko.spring_sec_demo.service;

import com.telusko.spring_sec_demo.dao.UserRepo;
import com.telusko.spring_sec_demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// 비즈니스 로직 담당 서비스 빈
@Service
public class UserService {

    // 유저 데이터 접근 레포지토리 주입
    @Autowired
    private UserRepo repo;

    // BCrypt 암호화기 생성 (강도 12 설정)
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // 회원 정보 암호화 저장 메서드
    public User saveUser(User user) {
        // 원문 비밀번호 암호화 및 재설정
        user.setPassword(encoder.encode(user.getPassword()));
        // 암호화 결과 콘솔 출력
        System.out.println(user.getPassword());
        // DB 데이터 최종 저장 및 반환
        return repo.save(user);
    }
}
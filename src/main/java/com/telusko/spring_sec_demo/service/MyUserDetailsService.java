package com.telusko.spring_sec_demo.service;

import com.telusko.spring_sec_demo.dao.UserRepo;
import com.telusko.spring_sec_demo.model.User;
import com.telusko.spring_sec_demo.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 서비스 빈 등록
@Service
public class MyUserDetailsService implements UserDetailsService {

    // DB 접근용 레포지토리 주입
    @Autowired
    private UserRepo repo;

    // 사용자 이름으로 상세 정보 조회 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 사용자명으로 데이터 조회
        User user = repo.findByUsername(username);

        // 사용자 데이터 부재 시 예외 발생
        if (user == null) {
            System.out.println("User 404"); // 로그 출력
            throw new UsernameNotFoundException("User 404"); // 예외 던짐
        }

        // DB 객체를 시큐리티 전용 객체로 변환 반환
        return new UserPrincipal(user);
    }
}
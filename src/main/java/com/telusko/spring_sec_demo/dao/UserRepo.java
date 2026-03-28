package com.telusko.spring_sec_demo.dao;

import com.telusko.spring_sec_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DB 창고 관리용 마법 인터페이스 정의
public interface UserRepo extends JpaRepository<User, Integer> {

    // 이름 기반 사용자 검색 자동화 메서드
    User findByUsername(String username);
}
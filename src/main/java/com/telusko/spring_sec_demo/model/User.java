package com.telusko.spring_sec_demo.model;

import jakarta.persistence.*;
import lombok.Data;

// Getter, Setter, toString 자동 생성
@Data
// DB 테이블 매핑 객체 선언
@Entity
// 연결 대상 DB 테이블 이름 지정
@Table(name = "users")
public class User {
    // 데이터 고유 식별자 설정
    @Id
    // 식별자 자동 생성 방식 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 사용자 로그인 아이디 저장 변수
    private String username;

    // 암호화된 비밀번호 저장 변수
    private String password;
}
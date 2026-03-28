package com.telusko.spring_sec_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// Getter, Setter, toString 등 자동 생성
@Data
// DB 테이블과 매핑되는 객체 선언
@Entity
// 실제 DB 내 테이블 이름 지정
@Table(name = "users")
public class User {
    // 기본키(Primary Key) 설정
    @Id
    private int id;
    // 사용자 로그인 아이디 저장 변수
    private String username;
    // 암호화된 비밀번호 저장 변수
    private String password;
}
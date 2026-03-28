package com.telusko.spring_sec_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Getter, Setter, toString, equals 등 필수 메서드 자동 생성
@Data
// 모든 필드를 매개변수로 받는 생성자 자동 생성
@AllArgsConstructor
// 매개변수가 없는 기본 생성자 자동 생성
@NoArgsConstructor
public class Student {
    // 학생 고유 식별 번호
    private int id;
    // 학생 이름 저장 변수
    private String name;
    // 학습 중인 기술 스택 명칭
    private String tech;
}
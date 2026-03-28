package com.telusko.spring_sec_demo.controller;

import com.telusko.spring_sec_demo.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// JSON 데이터 반환 컨트롤러
@RestController
public class StudentController {

    // 가상 학생 저장소 (DB 대용)
    List<Student> students = new ArrayList<>(List.of(
            new Student(1, "Navin", "Java"),
            new Student(2, "Kiran", "Blockchain")
    ));

    // 현재 세션의 CSRF 토큰 확인 메서드
    @GetMapping("csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        // 요청 속성에 담긴 CSRF 토큰 반환
        return (CsrfToken) request.getAttribute("_csrf");
    }

    // 학생 전체 목록 조회
    @GetMapping("students")
    public List<Student> getStudents() {
        // 학생 리스트 전체 반환
        return students;
    }

    // 새로운 학생 등록
    @PostMapping("students")
    public void addStudent(@RequestBody Student student) {
        // 리스트에 새 학생 추가
        students.add(student);
    }
}
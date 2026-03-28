package com.telusko.spring_sec_demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// 스프링 시큐리티 전용 사용자 정보 규격 구현
public class UserPrincipal implements UserDetails {

    // 실제 데이터베이스 사용자 엔티티
    private User user;

    // 생성자 통한 데이터 주입
    public UserPrincipal(User user) {
        this.user = user;
    }

    // 사용자 권한 목록 반환 (USER 권한 고정 부여)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 단일 권한 객체 생성 반환
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    // 암호화된 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자 아이디 반환
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부 확인 (true: 사용 가능)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 확인 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명(비번) 만료 여부 확인 (true: 유효함)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 확인 (true: 활성 상태)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
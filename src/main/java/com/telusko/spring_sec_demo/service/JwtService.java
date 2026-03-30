package com.telusko.spring_sec_demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// JWT 발급 및 관리 서비스
@Service
public class JwtService {

    // (미사용) 고정 비밀키 문자열
    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZyZ1B1cnBvc2VzMTIzNDU2Nzg=\r\n";

    // 동적 생성 비밀키 저장 변수
    private String secretKey;

    // 객체 생성 시 비밀키 자동 초기화
    public JwtService() {
        secretKey = generateSecretKey();
    }

    // 무작위 256비트 비밀키 생성 로직
    public String generateSecretKey() {
        try {
            // HMAC-SHA256 암호화 알고리즘 지정
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            // 키 생성
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("Secret Key : " + secretKey.toString());
            // Base64 형식 문자열 변환 반환
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    // 사용자 이름 기반 JWT 생성 메서드
    public String generateToken(String username) {
        // 추가 정보(Claim) 저장용 맵
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                // 클레임 정보 삽입
                .setClaims(claims)
                // 발급 대상자(아이디) 지정
                .setSubject(username)
                // 토큰 발급 일시 지정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 토큰 만료 일시 지정 (+3분)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                // 암호화 키 기반 서명 추가
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    // 문자열 비밀키의 암호화 객체 변환 로직
    private Key getKey() {
        // Base64 디코딩 처리
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // 서명용 키 객체 반환
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 내 사용자 이름(Subject) 추출
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 특정 클레임(정보) 추출 범용 메서드
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // 토큰 해독 및 전체 클레임 추출 로직
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                // 서버의 비밀키로 서명 검증 세팅
                .setSigningKey(getKey())
                .build()
                // 토큰 문자열 해독 및 파싱
                .parseClaimsJws(token)
                // 알맹이(Body) 데이터 반환
                .getBody();
    }

    // 토큰 유효성 최종 검증 (이름 일치 및 만료 확인)
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 내 만료 시간(Expiration) 추출
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
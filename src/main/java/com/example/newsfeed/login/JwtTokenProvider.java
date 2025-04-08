package com.example.newsfeed.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT 토큰을 생성하고, 검증하고, 안의 유저 정보(email 등)를 꺼내주는 클래스
@Component
public class JwtTokenProvider {

    private final String secretKey = "yourSecretKey"; // 비밀키 (절대 공개 불가)
    private final long tokenValidTime = 60 * 60 * 1000L; // 유효시간 1시간(밀리초)

    // 토큰 생성
    public String createToken(String email) {

        Date now = new Date(); // 현재시간

        return Jwts.builder()
                .setSubject(email) // JWT payload에 들어갈 내용
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // 암호화 방식 + 시크릿키
                .compact(); // JWT 문자열로 변환 (subject >> 토큰이 어떤 유저에 대한 것인지 식별하는 값)
    }

    // 토큰에서 사용자 이메일 추출
    public String getUserEmail(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 우리가 토큰에 넣은 email
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 유효하지 않으면 false 반환
        }
    }

    // 로그아웃을 위한 토큰 만료시간 계산 메서드
    public long getExpiration(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    // 클라이언트 요청에서 JWT 토큰을 꺼내는 핵심 로직
    public String resolveToken(HttpServletRequest request) {

        // 클라이언트가 서버에 요청 보낼 때, JWT 토큰은 보통 HTTP 헤더에 담김 >> "Authorization(헤더): Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." 변수에 bearer ey... 문자열 담김
        String bearer = request.getHeader("Authorization");

        // 헤더가 아예 없는 요청일 수 있으니 null이면 건너뛰고, "Bearer "로 시작하는지 확인
        if (bearer != null && bearer.startsWith("Bearer")) {
            return bearer.substring(7); // "Bearer "는 총 7글자 → 앞의 "Bearer "를 잘라내고 토큰만 반환
        }

        return null; // Authorization 헤더가 없거나, "Bearer "로 시작하지 않으면 null 반환
    }

}

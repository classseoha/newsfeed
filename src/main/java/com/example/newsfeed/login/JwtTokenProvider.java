package com.example.newsfeed.login;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 관련 기능을 모아놓은 도우미 클래스 (JWT 생성, 해석, 유효성 검사, 만료시간 계산, 헤더에서 추출까지 모두 처리)
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // JWT에 서명할 비밀키 (토큰 위조 방지) >> 절대 노출되면 안됨, 보통 .env, application.yml에 따로 둠
    private final long tokenValidTime; // 토큰 유효시간 1시간(밀리초 기준)

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token-valid-time}") long tokenValidTime) {
        this.secretKey = secretKey;
        this.tokenValidTime = tokenValidTime;
    }

    // 이메일을 JWT로 변환해주는 메서드 (Header.Payload.Signature)
    public String createToken(String email) {

        Date now = new Date(); // 현재시간

        return Jwts.builder()
                .setSubject(email) // JWT payload에 들어갈 내용
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // 암호화 방식(HS256) + 비밀키
                .compact(); // JWT 문자열로 변환 (subject >> 토큰이 어떤 유저에 대한 것인지 식별하는 값)
    }

    // 토큰에서 사용자 이메일 추출 (Payload의 sub 값)
    public String getUserEmail(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 우리가 토큰에 넣은 email
    }

    // 토큰 유효성 검증 (예외를 던지도록 변경)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException ex) {
            log.warn("잘못된 JWT 서명입니다: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("만료된 JWT 토큰입니다: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("지원되지 않는 JWT 토큰입니다: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT 토큰이 비었습니다: {}", ex.getMessage());
        }
        return false;
    }

    // 로그아웃을 위한 토큰 만료시간 계산 메서드
    public long getExpiration(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis(); // 토큰 만료 시간에서 현재 시간을 뺌 >> 남은 시간(ms)
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

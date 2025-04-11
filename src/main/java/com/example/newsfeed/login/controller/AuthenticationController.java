package com.example.newsfeed.login.controller;

import com.example.newsfeed.entity.User;
import com.example.newsfeed.login.JwtTokenProvider;
import com.example.newsfeed.login.dto.LoginRequestDto;
import com.example.newsfeed.login.dto.LoginResponseDto;
import com.example.newsfeed.user.repository.UserRepository;
import com.example.newsfeed.user.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

// 로그인, 로그아웃 요청 처리 컨트롤러
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor // final 필드들을 자동으로 생성자 주입

public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider; // JWT를 생성하거나 검증하는 유틸 클래스
    private final UserService userService; // 이메일로 사용자 조회하거나 비밀번호 체크하는 비즈니스 로직 담당
    private final RedisTemplate<String, String> redisTemplate; // Redis에 토큰 저장/조회하는 도구 (로그아웃 시 사용)
    private final UserRepository userRepository;


    // 로그인 성공 시 토큰을 응답으로 줘서 클라이언트가 저장함, 이후 요청부터는 이 토큰을 Authorization 헤더에 담아서 전송
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        String email = request.getEmail();

        // 이메일 형식 검증
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("유효한 이메일 형식이 아닙니다.");
        }

        // Redis에 해당 이메일로 로그인된 기록이 있다면 예외 처리
        String loginKey = "login:" + email;
        String existingToken = redisTemplate.opsForValue().get(loginKey);
//        if (existingToken != null) {
//            throw new IllegalStateException("이미 로그인 상태입니다.");
//        }

        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 유저가 존재하지 않습니다."));

        // 입력된 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail()); // JWT 토큰 생성 (이메일을 기반으로)

        // Redis에 로그인 상태 저장 (1시간 동안)
        redisTemplate.opsForValue().set(loginKey, token, jwtTokenProvider.getExpiration(token), TimeUnit.MILLISECONDS);

        return ResponseEntity.ok(new LoginResponseDto(token)); // 클라이언트에게 응답으로 전달 >> 이후 이 토큰을 요청 헤더에 담아야 인증 됨
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request); // Authorization 헤더에서 토큰을 꺼냄 (resolveToken)

        // 토큰 존재 여부 확인
        if (token == null) {
            throw new AuthenticationCredentialsNotFoundException("토큰이 존재하지 않습니다.");
        }

        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }

        // 이메일 추출
        String email = jwtTokenProvider.getUserEmail(token);
        String loginKey = "login:" + email;

        // Redis에 로그인 상태가 있는 경우 제거
        if (redisTemplate.hasKey(loginKey)) {
            redisTemplate.delete(loginKey);
        }

        // Redis에 로그아웃 처리
        long expiration = jwtTokenProvider.getExpiration(token); // 토큰 만료 시간
        redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS); // 블랙리스트 등록

        return ResponseEntity.ok("로그아웃 성공");
    }

}

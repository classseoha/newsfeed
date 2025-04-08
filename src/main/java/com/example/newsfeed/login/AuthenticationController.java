package com.example.newsfeed.login;

import com.example.newsfeed.login.dto.LoginRequestDto;
import com.example.newsfeed.login.dto.LoginResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
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


    // 로그인 성공 시 토큰을 응답으로 줘서 클라이언트가 저장함, 이후 요청부터는 이 토큰을 Authorization 헤더에 담아서 전송
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        User user = userService.findByEmail(request.getEmail()); // 로그인 요청에서 받은 이메일로 DB에서 유저를 찾아옴

        // 입력된 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교 >> 실패 시 예외 발생
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail()); // JWT 토큰 생성 (이메일을 기반으로)

        return ResponseEntity.ok(new LoginResponseDto(token)); // 클라이언트에게 응답으로 전달 >> 이후 이 토큰을 요청 헤더에 담아야 인증 됨
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request); // Authorization 헤더에서 토큰을 꺼냄 (resolveToken)

        // 토큰이 유효한지도 체크 (validateToken)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            long expiration = jwtTokenProvider.getExpiration(token); // 토큰의 남은 만료 시간 계산
            redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS); // Redis에 "토큰" -> "logout" 형태로 저장
        }

        return ResponseEntity.ok("로그아웃 성공");
    }

}

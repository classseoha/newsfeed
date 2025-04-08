package com.example.newsfeed.login;

import com.example.newsfeed.login.dto.LoginRequestDto;
import com.example.newsfeed.login.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

// 로그인 처리 컨트롤러
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;


    // 로그인 성공 시 토큰을 응답으로 줘서 클라이언트가 저장함, 이후 요청부터는 이 토큰을 Authorization 헤더에 담아서 전송
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        User user = userService.findByEmail(request.getEmail()); // 이메일로 유저 조회

        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail()); // 토큰 생성

        return ResponseEntity.ok(new LoginResponseDto(token)); // 토큰 응답으로 전달
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            long expiration = jwtTokenProvider.getExpiration(token);
            redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

}

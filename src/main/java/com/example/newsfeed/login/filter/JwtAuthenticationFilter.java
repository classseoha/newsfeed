package com.example.newsfeed.login.filter;

import com.example.newsfeed.login.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청마다 JWT 토큰이 있으면 확인하고, 로그인 상태로 만들어주는 역할(JWT 인증을 처리하는 핵심 필터 클래스 >> 유저 정보를 SecurityContext에 넣어줌)
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter >> 한 요청 당 한 번만 실행하는 필터

    // JWT 검증 도구와 유저 정보를 불러올 도구를 주입
    private final JwtTokenProvider jwtTokenProvider; // JWT 생성/검증 도구 클래스
    private final UserDetailsService userDetailsService; // 이메일로 유저 정보(UserDetails)를 가져옴
    private final RedisTemplate<String, String> redisTemplate; // Redis 접근용 → 로그아웃된 토큰인지 확인

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, RedisTemplate<String, String> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/authentication/login") || path.equals("/users/signup");
    }

    // 필터의 핵심 로직: 요청이 들어올 때마다 토큰을 꺼내서 검증하고 유저 인증을 넣어줌
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request); // 헤더에서 Bearer <토큰> 형식의 문자열에서 토큰만 꺼내는 함수

        // 토큰이 null이거나 이상한 형식이면 그냥 다음 필터로 넘김
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그아웃된 토큰인지 확인
        if (redisTemplate.hasKey(token)) {
            throw new AuthenticationCredentialsNotFoundException("로그아웃된 토큰입니다.");
        }

        // 유효성 검사 (토큰 만료 포함)
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationCredentialsNotFoundException("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 이메일로 유저 정보를 찾아서 Authentication 객체를 생성 (서명 검증, 만료 체크 등 토큰이 올바른지 검사)
        String email = jwtTokenProvider.getUserEmail(token); // 토큰에서 이메일 꺼냄
        UserDetails userDetails = userDetailsService.loadUserByUsername(email); // 이메일로 UserDetails 가져옴

        // Security 인증 객체 생성 (비번은 null, 권한은 그대로 >> Spring Security에게 이 요청은 이 유저가 한거야 라고 알려주는 부분)
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContext에 인증 정보 등록 (이제 이 요청은 인증된 사용자의 요청으로 간주)
        // 이걸 등록하지 않으면 컨트롤러에서 @AuthenticationPrincipal 같은 걸로 유저 정보 못 씀
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 넘기기 (인증을 마치고 나면 필터 체인을 다음으로 넘김)
        filterChain.doFilter(request, response);
    }
}

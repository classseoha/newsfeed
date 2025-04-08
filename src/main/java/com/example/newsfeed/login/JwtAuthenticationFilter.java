package com.example.newsfeed.login;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 모든 요청이 들어올 때, JWT 토큰을 검사하고, 유저 정보를 SecurityContext에 넣어주는 필터
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter >> 한 요청 당 한 번만 동작하는 필터

    // JWT 검증 도구와 유저 정보를 불러올 도구를 주입
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsService userDetailsService,
                                   RedisTemplate<String, String> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }



    // 필터의 핵심 로직: 요청이 들어올 때마다 토큰을 꺼내서 검증하고 유저 인증을 넣어줌
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request); // 헤더에서 Bearer <토큰> 형식의 문자열에서 토큰만 꺼내는 함수

        // 🔥 로그아웃된 토큰인지 확인
        if (token != null && redisTemplate.hasKey(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 토큰입니다.");
            return;
        }

        // 유효한 토큰이면 이메일로 유저 정보를 찾아서 Authentication 객체를 생성
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getUserEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Spring Security에게 이 요청은 이 유저가 한거야 라고 알려주는 부분
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}

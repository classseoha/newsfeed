package com.example.newsfeed.login.config;

import com.example.newsfeed.login.JwtAuthenticationFilter;
import com.example.newsfeed.login.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// Spting Security 설정 담당 클래스
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 안씀
                .authorizeHttpRequests(auth -> auth // URL 별 인증 설정
                        .requestMatchers("/authentication/**").permitAll() // 해당 URL로 시작하는 로그인, 회원가입은 인증 없이 허용
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 끼워 넣기
                );

        return http.build(); // SecurityFilterChain 객체로 완성해서 반환
    }
}

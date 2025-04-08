package com.example.newsfeed.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 로그인 성공 시 서버가 클라이언트에게 토큰 내려주는 DTO
@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private final String token;

}

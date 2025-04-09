package com.example.newsfeed.friends.dto;

import lombok.Getter;

@Getter
public class RelationRequestDto {
    private String email;        // 내 이메일
    private String targetEmail;  // 친구 요청할 대상 이메일
}

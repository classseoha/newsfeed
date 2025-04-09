package com.example.newsfeed.friends.dto;

import com.example.newsfeed.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class RelationResponseDto {
    private String email;
    private String nickname;
    private int age;
    private Character gender;
    private String image;
    private LocalDateTime updatedAt;

    public static RelationResponseDto from(User user) {
        return RelationResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(LocalDate.now().getYear() - user.getBirthDate().getYear() + 1)
                .gender(user.getGender())
                .updatedAt(user.getModifiedAt())
                .build();
    }
}

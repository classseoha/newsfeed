package com.example.newsfeed;

import com.example.newsfeed.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private LocalDate birthDate;
    private String nickname;
    private Character gender;
    private LocalDateTime createdAt;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.createdAt = user.getCreatedAt();
    }
}

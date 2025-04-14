package com.example.newsfeed.user.dto;

import com.example.newsfeed.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {

    private final String email;
    private final String nickname;
    private final LocalDate birthDate;
    private final Character gender;
    private final String image;


    public UserResponseDto(String email, String nickname, LocalDate birthDate, Character gender, String image) {
        this.email = email;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.image = image;
    }

    public UserResponseDto(User user) {

        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.image = user.getImage();
    }
}

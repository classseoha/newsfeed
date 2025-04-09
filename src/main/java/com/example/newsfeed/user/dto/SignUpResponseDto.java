package com.example.newsfeed.user.dto;

import com.example.newsfeed.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpResponseDto {

    private final String email;
    private final String nickname;
    private final LocalDate birthDate;
//    private final String password;
    private final Character gender;
    private final String image;

    public SignUpResponseDto(
            String email, String nickname,
            LocalDate birthDate, Character gender, String image
    ) {
        this.email = email;
        this.nickname = nickname;
//        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.image = image;
    }

    public SignUpResponseDto(User user){
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.image = user.getImage();
    }
}

package com.example.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class SignUpRequestDto {

    private final String email;
    private final String nickname;
    private final String password;
    private final LocalDate birthDate;
    private final Character gender;
    private final String image;

@JsonCreator
    public SignUpRequestDto(
        @JsonProperty("email") String email,  @JsonProperty("nickname") String nickname, @JsonProperty("password") String password,
        @JsonProperty("birthDate") LocalDate birthDate,  @JsonProperty("gender") Character gender,  @JsonProperty("image") String image
    ) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.image = image;
    }
}

package com.example.newsfeed.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateUserResquestDto {
    private final String nickname;
    private final String image;
    private final LocalDate birthday;

    @JsonCreator
    public UpdateUserResquestDto(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("image") String image,
            @JsonProperty("birthday") LocalDate birthday) {
        this.nickname = nickname;
        this.image = image;
        this.birthday = birthday;
    }
}

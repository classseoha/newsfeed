package com.example.newsfeed.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateUserResquestDto {
    private final String nickname;

    @JsonCreator
    public UpdateUserResquestDto(
            @JsonProperty("nickname") String nickname
    ) {
        this.nickname = nickname;
    }
}

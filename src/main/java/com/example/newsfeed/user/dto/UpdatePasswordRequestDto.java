package com.example.newsfeed.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdatePasswordRequestDto {

    private final String oldPassword;
    private final String newPassword;

    @JsonCreator
    public UpdatePasswordRequestDto(@JsonProperty("oldPassword") String oldPassword,@JsonProperty("newPassword") String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

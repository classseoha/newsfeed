package com.example.newsfeed.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DeleteUserRequestDto {

        private final String password;

        @JsonCreator
        public DeleteUserRequestDto(@JsonProperty("password") String password) {
            this.password = password;
        }
}

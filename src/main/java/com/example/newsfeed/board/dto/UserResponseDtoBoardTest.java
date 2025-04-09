package com.example.newsfeed.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDtoBoardTest {
    private String email;
    private Integer age;
    private String nickname;
    private Character gender;
}

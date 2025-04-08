package com.example.newsfeed.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RelationResponseDto {

    private String email;
    private String nickname;
    private Character gender;
    private String image;
}

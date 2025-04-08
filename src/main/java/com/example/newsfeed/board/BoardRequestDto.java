package com.example.newsfeed.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BoardRequestDto {

    private String title;

    private String contents;

    private String image;

    @Setter
    private String email;

    public BoardRequestDto(String title, String contents, String image) {
        this.title = title;
        this.contents = contents;
        this.image = image;
    }
}

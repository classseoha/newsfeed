package com.example.newsfeed.board;

import com.example.newsfeed.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;

    private String title;

    private String contents;

    private LocalDateTime createAt;

    private String image;

    private String nickname;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createAt = board.getCreatedAt();
        this.image = board.getImage();
        this.nickname = board.getUser().getNickname();
    }
}

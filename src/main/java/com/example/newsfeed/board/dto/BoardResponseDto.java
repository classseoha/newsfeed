package com.example.newsfeed.board.dto;

import com.example.newsfeed.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponseDto {

    private Long id;

    private String nickname;

    private String title;

    private String contents;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getModifiedAt();
        this.image = board.getImage();
        this.nickname = board.getUser().getNickname();
    }
}

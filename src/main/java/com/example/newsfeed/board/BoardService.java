package com.example.newsfeed.board;

import com.example.newsfeed.UserResponseDto;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto findBoardById(Long id);

    UserResponseDto findBoardCreatorById(Long id);

    BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id);

    void deleteBoard(Long id);
}

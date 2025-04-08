package com.example.newsfeed.board;

import com.example.newsfeed.UserResponseDto;

import java.util.List;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto findBoardById(Long id, String email);

    UserResponseDto findBoardCreatorById(Long id);

    BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id);

    void deleteBoard(Long id);

    List<BoardResponseDto> findAllBoardsByMeAndFriends(String email);
}

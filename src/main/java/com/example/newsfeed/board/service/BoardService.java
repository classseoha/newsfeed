package com.example.newsfeed.board.service;

import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import com.example.newsfeed.board.dto.UserResponseDtoBoardTest;

import java.util.List;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto findBoardById(Long id, String email);

    UserResponseDtoBoardTest findBoardCreatorById(Long id);

    BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id);

    void deleteBoard(Long id);

    List<BoardResponseDto> findAllBoardsByMeAndFriends(String email);
}

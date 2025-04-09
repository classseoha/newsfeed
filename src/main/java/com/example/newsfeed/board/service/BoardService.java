package com.example.newsfeed.board.service;

import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import com.example.newsfeed.user.dto.UserResponseDto;

import java.util.List;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto findBoardById(Long id);

    BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id, String email);

    void deleteBoard(Long id);

    List<BoardResponseDto> findAllBoardsByMeAndFriends(String email);
}

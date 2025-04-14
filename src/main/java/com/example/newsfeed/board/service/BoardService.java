package com.example.newsfeed.board.service;

import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import org.springframework.data.domain.Page;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto);

    BoardResponseDto findBoardById(Long id, String email);

    BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id, String email);

    void deleteBoard(Long id, String email);

    Page<BoardResponseDto> findAllBoardsByMeAndFriends(String email, Integer page, Integer size);
}

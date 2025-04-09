package com.example.newsfeed.board.controller;

import com.example.newsfeed.UserResponseDto;
import com.example.newsfeed.board.service.BoardService;
import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boardRequestDto.setEmail(email);

        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }


    //뉴스피드 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> findAllBoardsByMeAndFriends(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<BoardResponseDto> allBoardsByMeAndFriends = boardService.findAllBoardsByMeAndFriends(email);

        return new ResponseEntity<>(allBoardsByMeAndFriends, HttpStatus.OK);
    }


    //게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findBoardById(@PathVariable Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        BoardResponseDto boardById = boardService.findBoardById(id, email);

        return new ResponseEntity<>(boardById, HttpStatus.OK);
    }

    //게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardRequestDto boardRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDto boardCreatorById = boardService.findBoardCreatorById(id);

        if(!boardCreatorById.getEmail().equals(email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        BoardResponseDto boardResponseDto = boardService.updateBoard(boardRequestDto, id);


        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }


    //게시글 삭제 -> 구현 방법으로 delete 사용과 exist 컬럼 사용하는 방법이 있을 것
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDto boardCreatorById = boardService.findBoardCreatorById(id);

        if(!boardCreatorById.getEmail().equals(email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        boardService.deleteBoard(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

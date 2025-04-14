package com.example.newsfeed.board.controller;

import com.example.newsfeed.board.service.BoardService;
import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<BoardResponseDto>> findAllBoardsByMeAndFriends(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Page<BoardResponseDto> allBoardsByMeAndFriends = boardService.findAllBoardsByMeAndFriends(email, page, size);

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

        BoardResponseDto boardResponseDto = boardService.updateBoard(boardRequestDto, id, email);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }


    //게시글 삭제 -> 구현 방법으로 delete 사용과 exist 컬럼 사용하는 방법이 있을 것
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boardService.deleteBoard(id, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.example.newsfeed.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest httpRequest){

        HttpSession session = httpRequest.getSession(false);

        // 로그인에서 session에 저장하는 정보가 무엇인지 확인 필요
        boardRequestDto.setNickname((String) session.getAttribute("sessionKey"));

        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }


    //뉴스피드 조회




    //게시글 상세 조회


    //게시글 수정


    //게시글 삭제 -> 구현 방법으로 delete 사용과 exist 컬럼 사용하는 방법이 있을 것
}

package com.example.newsfeed.controller;

import com.example.newsfeed.dto.SignUpRequestDto;
import com.example.newsfeed.dto.SignUpResponseDto;
import com.example.newsfeed.service.UserService;
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
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpConrtroller {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto, HttpServletRequest request)
    {
        SignUpResponseDto signUpResponseDto = userService.signup(requestDto);

        HttpSession session = request.getSession();
        session.setAttribute("user", requestDto.getNickname());

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }
}

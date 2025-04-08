package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.dto.SignUpRequestDto;
import com.example.newsfeed.user.dto.SignUpResponseDto;
import com.example.newsfeed.user.dto.UserResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.service.UserRepository;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
@RequiredArgsConstructor
public class UserConrtroller {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto, HttpServletRequest request)
    {
        SignUpResponseDto signUpResponseDto = userService.signup(requestDto);

        HttpSession session = request.getSession();
        session.setAttribute("user", requestDto.getNickname());

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable String email){
        UserResponseDto userResponseDto = userService.findByEmail(email);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{mypage}")
    public ResponseEntity<UserResponseDto> findByPage(HttpSession session) throws IllegalAccessException {

        String email = (String) session.getAttribute("email");

        if(email == null){
            throw new IllegalAccessException("로그인이 필요합니다.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalAccessException("해당 이메일의 유저가 없습니다."));

        return ResponseEntity.ok(new UserResponseDto(user));
    }
}

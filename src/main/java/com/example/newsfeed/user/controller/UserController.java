package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.dto.*;
import com.example.newsfeed.user.repository.UserRepository;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //403에러
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto, HttpServletRequest request)
    {
        SignUpResponseDto signUpResponseDto = userService.signup(requestDto);

        HttpSession session = request.getSession();
        session.setAttribute("user", requestDto.getNickname());

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

//    //회원조회
//    @GetMapping("/{email}")
//    public ResponseEntity<UserResponseDto> findById(@PathVariable String email){
//        UserResponseDto userResponseDto = userService.findByEmail(email);
//
//        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//    }

    //마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<UserResponseDto> findByPage()  {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDto userResponseDto = userService.findByEmail(email);
        return ResponseEntity.ok(userResponseDto);
    }

    //비밀번호 수정
    @PatchMapping()
    public ResponseEntity<Void> updatePassword(
            @RequestBody UpdatePasswordRequestDto requestDto
            ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.updatePassword(email, requestDto.getOldPassword(), requestDto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //회원정보 수정
    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserResquestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.updateUser(email, requestDto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

//    회원탈퇴
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestBody UpdateUserResquestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.delete(email);

        return ResponseEntity.ok("회원 탈퇴 완료되었습니다.");
        }
}

package com.example.newsfeed.user.service;

import com.example.newsfeed.user.dto.SignUpRequestDto;
import com.example.newsfeed.user.dto.SignUpResponseDto;
import com.example.newsfeed.user.dto.UpdateUserResquestDto;
import com.example.newsfeed.user.dto.UserResponseDto;
import com.example.newsfeed.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

//회원가입 User 서비스 구현

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    //회원가입
    public SignUpResponseDto signup(SignUpRequestDto requestDto)
    {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();
        Character gender = requestDto.getGender();
        String image = requestDto.getImage();
        LocalDate birthDate = requestDto.getBirthDate();

        if(email == null || email.isBlank() || !email.contains("@")){
            throw new IllegalArgumentException("유효한 이메일을 입력해주세요.");

        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if(nickname == null || nickname.isBlank()){
            throw new IllegalArgumentException("닉네임을 입력해주세요 .");
        }



        User user = new User(email, password, nickname, birthDate, gender, image);

        User savedUser = userRepository.save(user);
        return new SignUpResponseDto(
                savedUser.getEmail(),
                savedUser.getNickname(),
                savedUser.getBirthDate(),
                savedUser.getGender(),
                savedUser.getImage()
        );
    }

    //회원조회
    public UserResponseDto findById(long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id : " + id);
        }
        User findUser = optionalUser.get();

        return new UserResponseDto(findUser.getEmail(), findUser.getNickname(),findUser.getBirthDate(),findUser.getGender(),findUser.getImage());
    }

    //마이페이지조회
    public UserResponseDto findByEmail(String email) {

       User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 존재하지 않습니다."));

        return new UserResponseDto(user);


    }

//    public User findByEmail2(String email){
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 없습니다."));
//    }

//    public boolean checkPassword(String reqPassword, String endcodePassword){
//        return passwordEncoder.matches(reqPassword, endcodePassword);
//    }

    //비밀번호 수정
    @Transactional
    public void updatePassword(String email, String oldPassword, String newPassword) {
        User findUser = userRepository.findByIdOrElseThrow(email);

       if(!findUser.getPassword().equals(oldPassword)){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
       }

       findUser.updatePassword(newPassword);
    }

    //회원정보수정
    @Transactional
    public void updateUser(String email, UpdateUserResquestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        user.setNickname(requestDto.getNickname());
        user.setImage(requestDto.getImage());
        user.setBirthDate(requestDto.getBirthday());
    }

    //회원탈퇴
    @Transactional
    public void delete(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("유저 없음"));
        userRepository.delete(user);
        userRepository.flush();

    }
}

package com.example.newsfeed.global.exception;

public class UserNotFoundException extends CustomException { // "User가 존재하지 않는 경우"를 명확히 표현하기 위한 커스텀 예외 클래스

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
    
    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}

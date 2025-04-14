package com.example.newsfeed.global.exception;

import lombok.Getter;

@Getter // @Getter를 통해 errorCode에 대한 getter 자동 생성
public class CustomException extends RuntimeException { // RuntimeException을 상속해서 체크 예외가 아닌 언체크 예외로 사용

    private final ErrorCode errorCode; // 예외가 발생했을 때 ErrorCode(예외에 담긴 비즈니스 에러 코드)를 함께 저장할 수 있게 만듬 >> 나중에 글로벌 예외 처리기에서 이 errorCode를 꺼내서 사용자 응답 생성에 활용함
    
    public CustomException(ErrorCode errorCode) { // ErrorCode의 기본 메시지를 예외 메시지로 설정함
        super(errorCode.getMessage()); // getMessage()는 "User Not Found"가 되고, errorCode는 USER_NOT_FOUND로 저장됨
        this.errorCode = errorCode;
    }
    
    public CustomException(ErrorCode errorCode, String message) { // 메시지를 직접 커스터마이징하고 싶을 때 사용 >> ex. new CustomException(ErrorCode.USER_NOT_FOUND, "유저 ID 123이 존재하지 않습니다.")

        super(message);
        this.errorCode = errorCode;
    }
}

package com.example.newsfeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "Bad Request", "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "Bad Request", "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error", "C004", "Internal Server Error"),
    INVALID_TYPE_VALUE(400, "Bad Request", "C005", "Invalid Type Value"),
    
    // User
    EMAIL_DUPLICATION(400, "Bad Request", "U001", "Email is Duplicated"),
    USER_NOT_FOUND(404, "Not Found", "U002", "User Not Found"),


    // Schedule
    SCHEDULE_NOT_FOUND(404, "Not Found", "S001", "Schedule Not Found");

    private final int status;
    private final String error;
    private final String code; // 개발자 또는 프론트엔드가 사용할 비즈니스 식별 코드 (ex. "C001": 공통 에러 / "U001": 사용자 관련 에러 / "S001" 등으로 더 확장 가능)
    private final String message; // 사용자나 개발자에게 보여줄 에러 메시지
}

package com.example.newsfeed.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse { // 글로벌 예외 처리에서 클라이언트에게 에러 정보를 반환할 때 사용하는 DTO

    private final LocalDateTime timestamp = LocalDateTime.now(); // 언제 발생했는지
    private final int status; // 상태코드 (예: 400, 404, 500 등)
    private final String error; // 에러 설명 (예: "Bad Request", "Internal Server Error")
    private final String code; // 에러 코드 >> 개발자가 지정한 비즈니스 코드 (예: "C001", "E404" 등)
    private final String message; // 메세지 >> 사용자에게 보여줄 메시지 또는 개발자가 지정한 상세 메시지
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // 해당 어노테이션 덕분에 null인 필드는 JSON 응답에서 제외돼, 응답 간결해짐
    private final List<FieldError> fieldErrors; // @Valid 실패 등에서 발생하는 필드 단위의 검증 오류 리스트 >> 비어 있으면 JSON에 포함되지 않도록 설정

    // Java에서는 생성자 대신 이렇게 of() 메서드를 제공해서 객체 생성을 더 명확하고 유연하게 할 수 있음 (빌더 패턴 + 정적 팩토리 메서드 조합)
    public static ErrorResponse of(ErrorCode errorCode) { // 일반적인 에러 반환용 (fieldErrors는 비어 있는 리스트로 세팅됨)
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .error(errorCode.getError())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .fieldErrors(new ArrayList<>())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, String message) { // message를 커스텀하게 바꾸고 싶을 때 사용
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .error(errorCode.getError())
                .code(errorCode.getCode())
                .message(message)
                .fieldErrors(new ArrayList<>())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors) { // 주로 @Valid 유효성 검사 실패 시 사용 (각 필드에서 어떤 값이 왜 잘못되었는지를 담음)
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .error(errorCode.getError())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .fieldErrors(fieldErrors)
                .build();
    }

    // 직접 만든 커스텀 클래스(내부 클래스) >> 필드에 대한 상세 오류 정보를 담음
    @Getter
    @Builder
    public static class FieldError {
        private String field; // 어떤 필드인지
        private String value; // 어떤 값이 들어왔는지
        private String reason; // 왜 잘못되었는지
        
        public static FieldError of(String field, String value, String reason) {
            return FieldError.builder()
                    .field(field)
                    .value(value)
                    .reason(reason)
                    .build(); // 위 세 정보를 받아 builder()로 객체를 만들어주는 팩토리 메서드
        }
    }
}

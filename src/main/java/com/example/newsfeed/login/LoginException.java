package com.example.newsfeed.login;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LoginException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());
        return new ResponseEntity<>("해당 이메일의 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException: {}", e.getMessage());
        return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return new ResponseEntity<>("유효한 이메일 형식이 아닙니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class, JwtException.class})
    public ResponseEntity<String> handleAuthenticationExceptions(Exception e) {
        log.error("AuthenticationException or JwtException: {}", e.getMessage());
        return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        log.error("IllegalStateException: {}", e.getMessage());
        return new ResponseEntity<>("이미 로그인 상태입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException e) {
        log.error("AuthenticationCredentialsNotFoundException: {}", e.getMessage());
        return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
    }
}

package com.urbannest.backend.exception;

import com.urbannest.backend.dto.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<AuthenticationResponse> handleEmailAlreadyTaken(EmailAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(AuthenticationResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }
}

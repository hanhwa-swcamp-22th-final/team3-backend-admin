package com.ohgiraffers.team3backendadmin.common.exception;

import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("Business exception", e);

        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation exception", e);

        String message = e.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(fieldError -> fieldError.getDefaultMessage())
            .orElse(ErrorCode.INVALID_INPUT.getMessage());

        ApiResponse<Void> response = ApiResponse.failure(ErrorCode.INVALID_INPUT.getCode(), message);
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Message not readable exception", e);

        ApiResponse<Void> response = ApiResponse.failure(
            ErrorCode.INVALID_INPUT.getCode(),
            ErrorCode.INVALID_INPUT.getMessage()
        );
        return ResponseEntity.status(ErrorCode.INVALID_INPUT.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception", e);

        ApiResponse<Void> response = ApiResponse.failure(
            ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
    }
}

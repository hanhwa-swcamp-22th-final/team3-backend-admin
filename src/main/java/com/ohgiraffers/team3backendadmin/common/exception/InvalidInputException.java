package com.ohgiraffers.team3backendadmin.common.exception;

public class InvalidInputException extends BusinessException {

    public InvalidInputException() {
        super(ErrorCode.INVALID_INPUT);
    }

    public InvalidInputException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }
}

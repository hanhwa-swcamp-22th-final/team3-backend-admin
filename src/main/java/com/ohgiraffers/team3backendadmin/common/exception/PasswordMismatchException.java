package com.ohgiraffers.team3backendadmin.common.exception;

public class PasswordMismatchException extends BusinessException {

    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_MISMATCH);
    }

    public PasswordMismatchException(String message) {
        super(ErrorCode.PASSWORD_MISMATCH, message);
    }
}

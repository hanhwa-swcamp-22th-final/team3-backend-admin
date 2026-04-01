package com.ohgiraffers.team3backendadmin.common.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("현재 비밀번호가 일치하지 않습니다");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}

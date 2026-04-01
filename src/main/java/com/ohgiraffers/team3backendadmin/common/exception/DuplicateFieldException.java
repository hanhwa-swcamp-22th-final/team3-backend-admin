package com.ohgiraffers.team3backendadmin.common.exception;

public class DuplicateFieldException extends RuntimeException {

    public DuplicateFieldException() {
        super("이미 사용 중인 값입니다");
    }

    public DuplicateFieldException(String message) {
        super(message);
    }
}

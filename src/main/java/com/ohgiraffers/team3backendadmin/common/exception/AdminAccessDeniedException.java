package com.ohgiraffers.team3backendadmin.common.exception;

public class AdminAccessDeniedException extends RuntimeException {

    public AdminAccessDeniedException() {
        super("해당 사원 정보를 찾을 수 없습니다");
    }

    public AdminAccessDeniedException(String message) {
        super(message);
    }
}

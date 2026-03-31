package com.ohgiraffers.team3backendadmin.common.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException() {
        super("해당 부서를 찾을 수 없습니다");
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}

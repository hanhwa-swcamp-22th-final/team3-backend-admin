package com.ohgiraffers.team3backendadmin.common.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException() {
        super("해당 사원을 찾을 수 없습니다");
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

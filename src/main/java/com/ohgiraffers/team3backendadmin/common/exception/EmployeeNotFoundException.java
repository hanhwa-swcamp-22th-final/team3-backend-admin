package com.ohgiraffers.team3backendadmin.common.exception;

public class EmployeeNotFoundException extends BusinessException {

    public EmployeeNotFoundException() {
        super(ErrorCode.EMPLOYEE_NOT_FOUND);
    }

    public EmployeeNotFoundException(String message) {
        super(ErrorCode.EMPLOYEE_NOT_FOUND, message);
    }
}

package com.ohgiraffers.team3backendadmin.common.exception;

public class DepartmentNotFoundException extends BusinessException {

    public DepartmentNotFoundException() {
        super(ErrorCode.DEPARTMENT_NOT_FOUND);
    }

    public DepartmentNotFoundException(String message) {
        super(ErrorCode.DEPARTMENT_NOT_FOUND, message);
    }
}

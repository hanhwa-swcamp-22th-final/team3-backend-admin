package com.ohgiraffers.team3backendadmin.common.exception;

public class EmployeeOnLeaveException extends BusinessException {

    public EmployeeOnLeaveException() {
        super(ErrorCode.EMPLOYEE_ON_LEAVE);
    }

    public EmployeeOnLeaveException(String message) {
        super(ErrorCode.EMPLOYEE_ON_LEAVE, message);
    }
}

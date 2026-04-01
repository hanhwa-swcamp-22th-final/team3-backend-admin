package com.ohgiraffers.team3backendadmin.common.exception;

public class DuplicateFieldException extends BusinessException {

    public DuplicateFieldException() {
        super(ErrorCode.DUPLICATE_FIELD);
    }

    public DuplicateFieldException(String message) {
        super(ErrorCode.DUPLICATE_FIELD, message);
    }
}

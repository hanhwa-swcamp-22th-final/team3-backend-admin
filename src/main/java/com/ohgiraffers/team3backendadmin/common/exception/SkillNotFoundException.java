package com.ohgiraffers.team3backendadmin.common.exception;

public class SkillNotFoundException extends BusinessException {

    public SkillNotFoundException() {
        super(ErrorCode.SKILL_NOT_FOUND);
    }

    public SkillNotFoundException(String message) {
        super(ErrorCode.SKILL_NOT_FOUND, message);
    }
}

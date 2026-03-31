package com.ohgiraffers.team3backendadmin.common.exception;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException() {
        super("해당 스킬 레코드를 찾을 수 없습니다");
    }

    public SkillNotFoundException(String message) {
        super(message);
    }
}

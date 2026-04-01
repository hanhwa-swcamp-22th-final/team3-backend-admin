package com.ohgiraffers.team3backendadmin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "BAD_REQUEST_001", "잘못된 요청입니다."),
    ADMIN_ACCESS_DENIED(HttpStatus.FORBIDDEN, "FORBIDDEN_001", "접근 권한이 없습니다."),

    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_001", "해당 사원을 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_002", "해당 부서를 찾을 수 없습니다."),
    SKILL_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_003", "해당 스킬 레코드를 찾을 수 없습니다."),
    FACTORY_LINE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_004", "해당 생산 라인을 찾을 수 없습니다."),
    EQUIPMENT_PROCESS_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_005", "해당 공정을 찾을 수 없습니다."),
    EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_006", "해당 설비를 찾을 수 없습니다."),
    ENVIRONMENT_STANDARD_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_007", "해당 환경 기준을 찾을 수 없습니다."),
    ENVIRONMENT_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_008", "해당 환경 이벤트를 찾을 수 없습니다."),
    EQUIPMENT_AGING_PARAM_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_009", "해당 설비 노후 파라미터를 찾을 수 없습니다."),
    MAINTENANCE_ITEM_STANDARD_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_010", "해당 유지보수 항목 기준을 찾을 수 없습니다."),
    MAINTENANCE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND_011", "해당 유지보수 이력을 찾을 수 없습니다."),

    ENVIRONMENT_STANDARD_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CONFLICT_001", "이미 사용 중인 환경 기준 코드입니다."),
    FACTORY_LINE_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CONFLICT_002", "이미 사용 중인 생산 라인 코드입니다."),
    EQUIPMENT_PROCESS_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CONFLICT_003", "이미 사용 중인 공정 코드입니다."),
    EQUIPMENT_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CONFLICT_004", "이미 사용 중인 설비 코드입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR_001", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmployeeUpdateRequest {

    @NotBlank(message = "사원 코드는 필수 입력 항목입니다")
    private final String employeeCode;

    private final String employeeName;

    private final String employeeEmail;

    private final String employeePhone;

    private final String employeeAddress;

    private final String employeeEmergencyContact;
}

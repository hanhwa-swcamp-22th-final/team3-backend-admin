package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmployeeDepartmentMatchRequest {

    @NotBlank(message = "사원 코드는 필수 입력 항목입니다")
    private final String employeeCode;

    @NotNull(message = "부서 ID는 필수 입력 항목입니다")
    private final Long departmentId;
}

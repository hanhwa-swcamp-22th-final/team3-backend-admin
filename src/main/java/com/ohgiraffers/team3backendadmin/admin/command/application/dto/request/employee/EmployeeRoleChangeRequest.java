package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class EmployeeRoleChangeRequest {

    @NotBlank(message = "대상 사원 코드는 필수 입력 항목입니다")
    private final String employeeCode;

    @NotNull(message = "변경할 역할은 필수 입력 항목입니다")
    private final EmployeeRole newRole;

    private final String reason;

    @NotNull(message = "적용일은 필수 입력 항목입니다")
    private final LocalDateTime effectiveDate;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmployeeCreateRequest {

    @NotNull(message = "부서 ID는 필수 입력 항목입니다")
    private final Long departmentId;

    @NotBlank(message = "사원명은 필수 입력 항목입니다")
    private final String employeeName;

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String employeeEmail;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다")
    private final String employeePhone;

    @NotBlank(message = "주소는 필수 입력 항목입니다")
    private final String employeeAddress;

    @NotBlank(message = "비상연락처는 필수 입력 항목입니다")
    private final String employeeEmergencyContact;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private final String employeePassword;

    @NotNull(message = "사원 역할은 필수 입력 항목입니다")
    private final EmployeeRole employeeRole;

    @NotNull(message = "사원 상태는 필수 입력 항목입니다")
    private final EmployeeStatus employeeStatus;

    @NotNull(message = "사원 등급은 필수 입력 항목입니다")
    private final EmployeeTier employeeTier;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDepartmentMatchResponse {
    private String employeeName;
    private String employeeCode;
    private Long departmentId;
}

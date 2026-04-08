package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRoleChangeResponse {
    private String employeeCode;
    private EmployeeRole newRole;
    private String reason;
    private LocalDateTime effectiveDate;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreateResponse {
    private String employeeCode;
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private String employeeAddress;
    private String employeeEmergencyContact;
    private String employeePassword;
    private EmployeeRole employeeRole;
    private EmployeeStatus employeeStatus;
    private EmployeeTier employeeTier;
    private LocalDate hireDate;
}

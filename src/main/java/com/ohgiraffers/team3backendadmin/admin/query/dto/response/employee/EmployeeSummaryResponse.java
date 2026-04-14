package com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EmployeeSummaryResponse {

    private String employeeName;
    private String employeeCode;
    private EmployeeRole employeeRole;
    private EmployeeStatus employeeStatus;
    private EmployeeTier employeeTier;
    private LocalDate hireDate;
    private String factoryLineName;
    private String equipmentName;
}

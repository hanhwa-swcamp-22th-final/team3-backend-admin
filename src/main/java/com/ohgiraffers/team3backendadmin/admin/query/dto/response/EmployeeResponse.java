package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeResponse {

    private String employeeCode;
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private String employeeAddress;
    private String employeeEmergencyContact;
    private EmployeeRole employeeRole;
    private EmployeeStatus employeeStatus;
    private EmployeeTier employeeTier;
}

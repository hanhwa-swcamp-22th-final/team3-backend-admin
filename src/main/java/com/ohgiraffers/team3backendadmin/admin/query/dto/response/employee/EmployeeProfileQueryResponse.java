package com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeProfileQueryResponse {

    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private Long departmentId;
    private String departmentName;
    private String teamName;
    private EmployeeTier currentTier;
    private BigDecimal totalScore;
    private LocalDate hireDate;
}

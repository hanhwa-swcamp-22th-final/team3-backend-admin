package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeTierUpdateRequest {

    private EmployeeTier tier;
}

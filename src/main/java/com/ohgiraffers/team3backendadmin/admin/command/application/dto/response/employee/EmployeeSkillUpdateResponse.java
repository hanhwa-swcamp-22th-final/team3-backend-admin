package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkillUpdateResponse {
    private String employeeCode;
    private BigDecimal equipmentResponse;
    private BigDecimal technicalTransfer;
    private BigDecimal innovationProposal;
    private BigDecimal safetyCompliance;
    private BigDecimal qualityManagement;
    private BigDecimal productivity;
}

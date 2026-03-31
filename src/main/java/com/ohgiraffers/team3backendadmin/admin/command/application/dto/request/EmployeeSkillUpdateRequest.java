package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class EmployeeSkillUpdateRequest {

    @NotBlank(message = "사원 코드는 필수 입력 항목입니다")
    private final String employeeCode;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal equipmentResponse;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal technicalTransfer;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal innovationProposal;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal safetyCompliance;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal qualityManagement;

    @DecimalMin(value = "0.00", message = "점수는 0.00 이상이어야 합니다")
    @DecimalMax(value = "100.00", message = "점수는 100.00 이하여야 합니다")
    private final BigDecimal productivity;
}

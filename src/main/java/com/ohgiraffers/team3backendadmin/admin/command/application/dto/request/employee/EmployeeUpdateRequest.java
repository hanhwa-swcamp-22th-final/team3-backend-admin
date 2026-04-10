package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class EmployeeUpdateRequest {

    @NotNull(message = "사원 ID는 필수 입력 항목입니다")
    private final Long employeeId;

    private final String employeeName;

    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String employeeEmail;

    private final String employeePhone;

    private final String employeeAddress;

    private final String employeeEmergencyContact;

    private final String employeePassword;

    private final EmployeeRole employeeRole;

    private final EmployeeStatus employeeStatus;

    private final EmployeeTier employeeTier;

    private final LocalDate hireDate;

    // ── 역량 점수 (선택 입력, null이면 0으로 초기화) ──

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

    public BigDecimal getScoreFor(SkillCategory category) {
        BigDecimal score = switch (category) {
            case EQUIPMENT_RESPONSE -> equipmentResponse;
            case TECHNICAL_TRANSFER -> technicalTransfer;
            case INNOVATION_PROPOSAL -> innovationProposal;
            case SAFETY_COMPLIANCE -> safetyCompliance;
            case QUALITY_MANAGEMENT -> qualityManagement;
            case PRODUCTIVITY -> productivity;
        };
        return score;
    }
}

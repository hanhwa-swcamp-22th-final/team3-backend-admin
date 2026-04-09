package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class EmployeeCreateRequest {

    @NotBlank(message = "사원명은 필수 입력 항목입니다")
    private final String employeeName;

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String employeeEmail;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다")
    private final String employeePhone;

    @NotBlank(message = "주소는 필수 입력 항목입니다")
    private final String employeeAddress;

    @NotBlank(message = "비상연락처는 필수 입력 항목입니다")
    private final String employeeEmergencyContact;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다")
    private final String employeePassword;

    @NotNull(message = "사원 역할은 필수 입력 항목입니다")
    private final EmployeeRole employeeRole;

    @NotNull(message = "사원 상태는 필수 입력 항목입니다")
    private final EmployeeStatus employeeStatus;

    @NotNull(message = "사원 등급은 필수 입력 항목입니다")
    private final EmployeeTier employeeTier;

    @NotNull(message = "입사일은 필수 입력 항목입니다")
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
        return score != null ? score : BigDecimal.ZERO;
    }
}

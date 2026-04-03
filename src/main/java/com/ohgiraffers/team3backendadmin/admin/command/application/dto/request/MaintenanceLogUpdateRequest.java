package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogUpdateRequest {
    @NotNull(message = "설비 ID는 필수입니다.")
    private Long equipmentId;

    @NotNull(message = "유지보수 항목 기준 ID는 필수입니다.")
    private Long maintenanceItemStandardId;

    @NotNull(message = "유지보수 유형은 필수입니다.")
    private MaintenanceType maintenanceType;

    @NotNull(message = "유지보수 일자는 필수입니다.")
    private LocalDate maintenanceDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "유지보수 점수는 음수일 수 없습니다.")
    private BigDecimal maintenanceScore;

    @DecimalMin(value = "0.0", inclusive = true, message = "ETA 유지보수 편차는 음수일 수 없습니다.")
    private BigDecimal etaMaintDelta;

    @NotNull(message = "유지보수 결과는 필수입니다.")
    private MaintenanceResult maintenanceResult;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceItemStandardUpdateRequest {
    @NotBlank(message = "유지보수 항목명은 필수입니다.")
    private String maintenanceItem;

    @NotNull(message = "유지보수 가중치는 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "유지보수 가중치는 음수일 수 없습니다.")
    private BigDecimal maintenanceWeight;

    @NotNull(message = "유지보수 최대 점수는 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "유지보수 최대 점수는 음수일 수 없습니다.")
    private BigDecimal maintenanceScoreMax;
}

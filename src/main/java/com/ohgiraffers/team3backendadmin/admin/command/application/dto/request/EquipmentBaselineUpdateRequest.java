package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentBaselineUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "기본 보증 성능은 음수일 수 없습니다.")
    private BigDecimal equipmentStandardPerformanceRate;

    @DecimalMin(value = "0.0", inclusive = true, message = "기본 오차율은 음수일 수 없습니다.")
    private BigDecimal equipmentBaselineErrorRate;

    @DecimalMin(value = "0.0", inclusive = true, message = "ETA 유지보수 값은 음수일 수 없습니다.")
    private BigDecimal equipmentEtaMaint;

    @DecimalMin(value = "0.0", inclusive = true, message = "설비 지수는 음수일 수 없습니다.")
    private BigDecimal equipmentIdx;
    private LocalDateTime equipmentBaselineCalculatedAt;
}

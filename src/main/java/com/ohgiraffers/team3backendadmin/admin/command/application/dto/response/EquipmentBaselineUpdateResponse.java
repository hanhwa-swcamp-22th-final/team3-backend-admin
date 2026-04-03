package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response;

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
public class EquipmentBaselineUpdateResponse {

    private Long equipmentBaselineId;
    private Long equipmentId;
    private Long equipmentAgingParamId;
    private BigDecimal equipmentStandardPerformanceRate;
    private BigDecimal equipmentBaselineErrorRate;
    private BigDecimal equipmentEtaMaint;
    private BigDecimal equipmentIdx;
    private LocalDateTime equipmentBaselineCalculatedAt;
}

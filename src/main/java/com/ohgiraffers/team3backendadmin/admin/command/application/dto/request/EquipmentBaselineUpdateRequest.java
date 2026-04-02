package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

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

    private BigDecimal equipmentStandardPerformanceRate;
    private BigDecimal equipmentBaselineErrorRate;
    private BigDecimal equipmentEtaMaint;
    private BigDecimal equipmentIdx;
    private LocalDateTime equipmentBaselineCalculatedAt;
}

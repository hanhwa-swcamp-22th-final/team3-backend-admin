package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentBaselineDetailResponse {

    private Long equipmentBaselineId;
    private Long equipmentId;
    private Long equipmentAgingParamId;
    private BigDecimal equipmentStandardPerformanceRate;
    private BigDecimal equipmentBaselineErrorRate;
    private BigDecimal equipmentEtaMaint;
    private BigDecimal equipmentIdx;
    private LocalDateTime equipmentBaselineCalculatedAt;
}



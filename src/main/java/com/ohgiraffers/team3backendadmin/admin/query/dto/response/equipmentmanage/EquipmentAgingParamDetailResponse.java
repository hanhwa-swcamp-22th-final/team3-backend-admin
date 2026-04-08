package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentAgingParamDetailResponse {

    private Long equipmentAgingParamId;
    private Long equipmentId;
    private BigDecimal equipmentEtaAge;
    private Integer equipmentWarrantyMonth;
    private Integer equipmentDesignLifeMonths;
    private BigDecimal equipmentWearCoefficient;
    private Integer equipmentAgeMonths;
    private LocalDateTime equipmentAgeCalculatedAt;
}



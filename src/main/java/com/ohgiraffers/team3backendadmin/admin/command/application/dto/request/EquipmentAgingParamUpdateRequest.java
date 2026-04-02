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
public class EquipmentAgingParamUpdateRequest {

    private BigDecimal equipmentEtaAge;
    private Integer equipmentWarrantyMonth;
    private Integer equipmentDesignLifeMonths;
    private BigDecimal equipmentWearCoefficient;
    private Integer equipmentAgeMonths;
    private LocalDateTime equipmentAgeCalculatedAt;
}

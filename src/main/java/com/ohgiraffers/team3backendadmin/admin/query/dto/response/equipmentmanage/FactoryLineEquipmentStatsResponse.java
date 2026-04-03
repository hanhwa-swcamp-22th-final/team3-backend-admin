package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FactoryLineEquipmentStatsResponse {

    private long totalEquipmentCount;
    private long operatingEquipmentCount;
    private BigDecimal operationRate;
}



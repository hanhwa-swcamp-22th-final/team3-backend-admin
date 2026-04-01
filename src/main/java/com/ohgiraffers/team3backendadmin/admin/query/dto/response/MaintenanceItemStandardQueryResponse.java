package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MaintenanceItemStandardQueryResponse {
    private Long maintenanceItemStandardId;
    private String maintenanceItem;
    private BigDecimal maintenanceWeight;
    private BigDecimal maintenanceScoreMax;
}

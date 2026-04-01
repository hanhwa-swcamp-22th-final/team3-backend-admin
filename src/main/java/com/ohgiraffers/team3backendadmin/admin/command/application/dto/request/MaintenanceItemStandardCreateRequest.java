package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceItemStandardCreateRequest {
    private String maintenanceItem;
    private BigDecimal maintenanceWeight;
    private BigDecimal maintenanceScoreMax;
}
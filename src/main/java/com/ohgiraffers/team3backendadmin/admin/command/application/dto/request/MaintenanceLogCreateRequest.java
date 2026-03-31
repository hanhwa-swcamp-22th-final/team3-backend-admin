package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogCreateRequest {
    private Long equipmentId;
    private Long maintenanceItemStandardId;
    private MaintenanceType maintenanceType;
    private LocalDate maintenanceDate;
    private BigDecimal maintenanceScore;
    private BigDecimal etaMaintDelta;
    private MaintenanceResult maintenanceResult;
}
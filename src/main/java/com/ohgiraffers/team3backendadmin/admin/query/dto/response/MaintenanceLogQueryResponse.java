package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceLogQueryResponse {
    private Long maintenanceLogId;
    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;
    private Long maintenanceItemStandardId;
    private String maintenanceItem;
    private MaintenanceType maintenanceType;
    private LocalDate maintenanceDate;
    private BigDecimal maintenanceScore;
    private BigDecimal etaMaintDelta;
    private MaintenanceResult maintenanceResult;
}

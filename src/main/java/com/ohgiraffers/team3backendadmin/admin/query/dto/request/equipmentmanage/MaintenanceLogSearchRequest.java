package com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MaintenanceLogSearchRequest {
    private Long equipmentId;
    private MaintenanceType maintenanceType;
    private MaintenanceResult maintenanceResult;
    private LocalDate exactMaintenanceDate;
    private LocalDate maintenanceDateFrom;
    private LocalDate maintenanceDateTo;
}



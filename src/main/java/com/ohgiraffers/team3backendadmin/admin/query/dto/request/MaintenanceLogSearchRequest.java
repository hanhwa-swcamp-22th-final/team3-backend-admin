package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MaintenanceLogSearchRequest {
    private Long equipmentId;
    private MaintenanceType maintenanceType;
    private MaintenanceResult maintenanceResult;
}

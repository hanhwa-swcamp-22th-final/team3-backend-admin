package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogCreateResponse {
    private Long maintenanceLogId;
    private Long equipmentId;
    private Long maintenanceItemStandardId;
    private MaintenanceType maintenanceType;
    private MaintenanceResult maintenanceResult;
}



package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentLatestSnapshotQueryResponse {

    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;
    private EquipmentStatus equipmentStatus;
    private EquipmentGrade equipmentGrade;
    private EquipmentGrade currentEquipmentGrade;
    private String equipmentProcessName;
    private String factoryLineName;
    private String environmentName;

    private Long latestEquipmentAgingParamId;
    private BigDecimal latestEquipmentEtaAge;
    private Integer latestEquipmentAgeMonths;
    private LocalDateTime latestEquipmentAgeCalculatedAt;

    private Long latestEquipmentBaselineId;
    private BigDecimal latestEquipmentStandardPerformanceRate;
    private BigDecimal latestEquipmentBaselineErrorRate;
    private BigDecimal latestEquipmentEtaMaint;
    private BigDecimal latestEquipmentIdx;
    private BigDecimal currentEquipmentIdx;
    private LocalDateTime latestEquipmentBaselineCalculatedAt;

    private Long latestEnvironmentEventId;
    private EnvDeviationType latestEnvDeviationType;
    private Boolean latestEnvCorrectionApplied;
    private LocalDateTime latestEnvDetectedAt;

    private Long latestMaintenanceLogId;
    private MaintenanceResult latestMaintenanceResult;
    private LocalDate latestMaintenanceDate;
}



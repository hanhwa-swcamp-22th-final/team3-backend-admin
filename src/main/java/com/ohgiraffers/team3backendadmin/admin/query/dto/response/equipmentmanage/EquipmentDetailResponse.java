package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentDetailResponse {

    private Long equipmentId;
    private Long equipmentProcessId;
    private Long environmentStandardId;
    private String equipmentCode;
    private String equipmentName;
    private EquipmentStatus equipmentStatus;
    private EquipmentGrade equipmentGrade;
    private LocalDateTime equipmentInstallDate;
    private String equipmentDescription;
    private String equipmentProcessName;
    private String factoryLineName;
    private String environmentCode;
    private String environmentName;
}


package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentQueryResponse {

    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;
    private EquipmentStatus equipmentStatus;
    private EquipmentGrade equipmentGrade;
    private String equipmentProcessName;
    private String factoryLineName;
    private String environmentName;
}
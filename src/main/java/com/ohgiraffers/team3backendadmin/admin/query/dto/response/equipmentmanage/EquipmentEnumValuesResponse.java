package com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EquipmentEnumValuesResponse {
    private List<EquipmentEnumOptionResponse> equipmentStatuses;
    private List<EquipmentEnumOptionResponse> equipmentGrades;
    private List<EquipmentEnumOptionResponse> environmentTypes;
}
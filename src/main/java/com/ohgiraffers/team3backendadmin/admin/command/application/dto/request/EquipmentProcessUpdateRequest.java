package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentProcessUpdateRequest {

    private Long factoryLineId;
    private String equipmentProcessCode;
    private String equipmentProcessName;
}
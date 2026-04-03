package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentProcessUpdateResponse {

    private Long equipmentProcessId;
    private Long factoryLineId;
    private String equipmentProcessCode;
    private String equipmentProcessName;
}

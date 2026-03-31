package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentProcessDetailResponse {

    private Long equipmentProcessId;
    private Long factoryLineId;
    private String factoryLineCode;
    private String factoryLineName;
    private String equipmentProcessCode;
    private String equipmentProcessName;
}

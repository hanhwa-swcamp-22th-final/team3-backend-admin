package com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EquipmentAgingParamSearchRequest {

    private Long equipmentId;
    private LocalDateTime exactCalculatedAt;
    private LocalDateTime calculatedFrom;
    private LocalDateTime calculatedTo;
}



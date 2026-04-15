package com.ohgiraffers.team3backendadmin.infrastructure.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HrMissionAssignmentRequest {

    private Long employeeId;
    private String currentTier;
}

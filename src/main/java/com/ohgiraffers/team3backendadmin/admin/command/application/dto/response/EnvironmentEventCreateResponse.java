package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentEventCreateResponse {
    private Long environmentEventId;
    private Long equipmentId;
    private EnvDeviationType envDeviationType;
    private Boolean envCorrectionApplied;
}

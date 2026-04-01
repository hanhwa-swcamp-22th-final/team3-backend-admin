package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnvironmentEventSearchRequest {
    private Long equipmentId;
    private EnvDeviationType envDeviationType;
}
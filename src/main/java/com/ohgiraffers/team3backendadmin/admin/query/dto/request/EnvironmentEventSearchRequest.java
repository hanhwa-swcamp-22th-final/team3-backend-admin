package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnvironmentEventSearchRequest {
    private Long equipmentId;
    private EnvDeviationType envDeviationType;
    private LocalDateTime exactDetectedAt;
    private LocalDateTime envDetectedFrom;
    private LocalDateTime envDetectedTo;
}

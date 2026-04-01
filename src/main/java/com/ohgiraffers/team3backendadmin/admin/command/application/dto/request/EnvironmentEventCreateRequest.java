package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentEventCreateRequest {
    private Long equipmentId;
    private BigDecimal envTemperature;
    private BigDecimal envHumidity;
    private Integer envParticleCnt;
    private EnvDeviationType envDeviationType;
    private Boolean envCorrectionApplied;
    private LocalDateTime envDetectedAt;
}
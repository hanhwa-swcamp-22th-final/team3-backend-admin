package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EnvironmentEventQueryResponse {
    private Long environmentEventId;
    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;
    private BigDecimal envTemperature;
    private BigDecimal envHumidity;
    private Integer envParticleCnt;
    private EnvDeviationType envDeviationType;
    private Boolean envCorrectionApplied;
    private LocalDateTime envDetectedAt;
}
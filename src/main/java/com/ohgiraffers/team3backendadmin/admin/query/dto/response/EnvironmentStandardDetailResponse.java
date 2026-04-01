package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EnvironmentStandardDetailResponse {
    private Long environmentStandardId;
    private EnvironmentType environmentType;
    private String environmentCode;
    private String environmentName;
    private BigDecimal envTempMin;
    private BigDecimal envTempMax;
    private BigDecimal envHumidityMin;
    private BigDecimal envHumidityMax;
    private Integer envParticleLimit;
}
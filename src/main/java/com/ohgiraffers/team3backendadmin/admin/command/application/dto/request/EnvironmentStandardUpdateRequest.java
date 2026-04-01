package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentStandardUpdateRequest {
    private EnvironmentType environmentType;
    private String environmentCode;
    private String environmentName;
    private BigDecimal envTempMin;
    private BigDecimal envTempMax;
    private BigDecimal envHumidityMin;
    private BigDecimal envHumidityMax;
    private Integer envParticleLimit;
}
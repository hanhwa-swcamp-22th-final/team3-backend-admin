package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @NotNull(message = "Environment type is required.")
    private EnvironmentType environmentType;

    @NotBlank(message = "Environment standard code is required.")
    private String environmentCode;

    @NotBlank(message = "Environment standard name is required.")
    private String environmentName;

    @NotNull(message = "Minimum temperature is required.")
    private BigDecimal envTempMin;

    @NotNull(message = "Maximum temperature is required.")
    private BigDecimal envTempMax;

    private BigDecimal envHumidityMin;

    private BigDecimal envHumidityMax;

    @PositiveOrZero(message = "Particle limit must be zero or positive.")
    private Integer envParticleLimit;

    @AssertTrue(message = "Humidity range is required for DRYROOM and CLEANROOM.")
    public boolean isHumidityRangeRequiredByType() {
        if (environmentType == null || environmentType == EnvironmentType.GENERAL) {
            return true;
        }

        return envHumidityMin != null && envHumidityMax != null;
    }

    @AssertTrue(message = "Particle limit is required for CLEANROOM.")
    public boolean isParticleLimitRequiredByType() {
        if (environmentType == null || environmentType != EnvironmentType.CLEANROOM) {
            return true;
        }

        return envParticleLimit != null;
    }
}

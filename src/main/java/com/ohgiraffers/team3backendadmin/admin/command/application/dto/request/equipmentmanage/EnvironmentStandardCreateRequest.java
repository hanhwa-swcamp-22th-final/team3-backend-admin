package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
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
public class EnvironmentStandardCreateRequest {
    @NotNull(message = "환경 유형은 필수입니다.")
    private EnvironmentType environmentType;

    @NotBlank(message = "환경 기준 코드는 필수입니다.")
    private String environmentCode;

    @NotBlank(message = "환경 기준명은 필수입니다.")
    private String environmentName;

    @NotNull(message = "최소 온도는 필수입니다.")
    private BigDecimal envTempMin;

    @NotNull(message = "최대 온도는 필수입니다.")
    private BigDecimal envTempMax;

    @NotNull(message = "최소 습도는 필수입니다.")
    private BigDecimal envHumidityMin;

    @NotNull(message = "최대 습도는 필수입니다.")
    private BigDecimal envHumidityMax;

    @NotNull(message = "입자 허용치는 필수입니다.")
    @PositiveOrZero(message = "입자 허용치는 음수일 수 없습니다.")
    private Integer envParticleLimit;
}
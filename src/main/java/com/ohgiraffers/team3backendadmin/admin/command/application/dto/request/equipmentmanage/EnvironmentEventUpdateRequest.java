package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class EnvironmentEventUpdateRequest {
    @NotNull(message = "설비 ID는 필수입니다.")
    private Long equipmentId;

    @DecimalMin(value = "0.0", inclusive = true, message = "온도는 음수일 수 없습니다.")
    private BigDecimal envTemperature;

    @DecimalMin(value = "0.0", inclusive = true, message = "습도는 음수일 수 없습니다.")
    private BigDecimal envHumidity;

    @PositiveOrZero(message = "입자 수는 음수일 수 없습니다.")
    private Integer envParticleCnt;

    @NotNull(message = "환경 이탈 유형은 필수입니다.")
    private EnvDeviationType envDeviationType;

    @NotNull(message = "조치 여부는 필수입니다.")
    private Boolean envCorrectionApplied;

    @NotNull(message = "감지 시각은 필수입니다.")
    private LocalDateTime envDetectedAt;
}
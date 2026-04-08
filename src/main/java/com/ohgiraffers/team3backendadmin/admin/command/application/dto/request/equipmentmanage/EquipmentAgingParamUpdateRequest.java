package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentAgingParamUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "ETA 노후도는 음수일 수 없습니다.")
    private BigDecimal equipmentEtaAge;

    @NotNull(message = "보증 개월 수는 필수입니다.")
    @PositiveOrZero(message = "보증 개월 수는 음수일 수 없습니다.")
    private Integer equipmentWarrantyMonth;

    @NotNull(message = "설계 수명 개월 수는 필수입니다.")
    @PositiveOrZero(message = "설계 수명 개월 수는 음수일 수 없습니다.")
    private Integer equipmentDesignLifeMonths;

    @NotNull(message = "마모 계수는 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "마모 계수는 음수일 수 없습니다.")
    private BigDecimal equipmentWearCoefficient;

    @PositiveOrZero(message = "경과 개월 수는 음수일 수 없습니다.")
    private Integer equipmentAgeMonths;
    private LocalDateTime equipmentAgeCalculatedAt;
}
package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class IndustryPresetCreateRequest {

    @NotBlank(message = "산업 프리셋 이름은 필수 입력 항목입니다")
    private final String industryPresetName;

    @NotNull(message = "가중치 V1은 필수 입력 항목입니다")
    private final BigDecimal weightV1;

    @NotNull(message = "가중치 V2는 필수 입력 항목입니다")
    private final BigDecimal weightV2;

    @NotNull(message = "가중치 V3는 필수 입력 항목입니다")
    private final BigDecimal weightV3;

    @NotNull(message = "가중치 V4는 필수 입력 항목입니다")
    private final BigDecimal weightV4;

    @NotNull(message = "알파 가중치는 필수 입력 항목입니다")
    private final BigDecimal alphaWeight;

    @NotNull(message = "적용일은 필수 입력 항목입니다")
    private final LocalDate effectiveDate;
}

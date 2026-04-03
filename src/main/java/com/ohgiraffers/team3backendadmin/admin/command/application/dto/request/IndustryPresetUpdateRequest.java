package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class IndustryPresetUpdateRequest {

    private final Long configId;

    private final String industryPresetName;

    private final BigDecimal weightV1;

    private final BigDecimal weightV2;

    private final BigDecimal weightV3;

    private final BigDecimal weightV4;

    private final BigDecimal alphaWeight;

    private final LocalDate effectiveDate;
}

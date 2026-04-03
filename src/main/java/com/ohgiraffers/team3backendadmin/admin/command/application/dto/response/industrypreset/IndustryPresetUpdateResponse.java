package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustryPresetUpdateResponse {
    private Long configId;
    private String industryPresetName;
    private BigDecimal weightV1;
    private BigDecimal weightV2;
    private BigDecimal weightV3;
    private BigDecimal weightV4;
    private BigDecimal alphaWeight;
    private LocalDate effectiveDate;
}

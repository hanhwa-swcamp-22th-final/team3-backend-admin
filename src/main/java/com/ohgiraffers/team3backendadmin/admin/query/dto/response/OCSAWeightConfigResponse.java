package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OCSAWeightConfigResponse {

    private Long configId;
    private String industryPresetName;
    private BigDecimal weightV1;
    private BigDecimal weightV2;
    private BigDecimal weightV3;
    private BigDecimal weightV4;
    private BigDecimal alphaWeight;
    private LocalDate effectiveDate;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}

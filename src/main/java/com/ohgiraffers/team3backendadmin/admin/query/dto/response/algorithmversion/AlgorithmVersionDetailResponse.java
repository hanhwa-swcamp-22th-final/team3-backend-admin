package com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlgorithmVersionDetailResponse {

    private Long algorithmVersionId;
    private String versionNo;
    private String implementationKey;
    private String description;
    private Boolean isActive;
    private String parameters;
    private String referenceValues;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmVersionCreateResponse {

    private Long algorithmVersionId;
    private String versionNo;
    private String implementationKey;
    private String description;
    private String policyConfig;
    private Boolean isActive;
}

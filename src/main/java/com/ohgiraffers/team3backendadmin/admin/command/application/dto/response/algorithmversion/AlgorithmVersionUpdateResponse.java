package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmVersionUpdateResponse {

    private Long algorithmVersionId;
    private String versionNo;
    private String implementationKey;
    private String description;
    private Boolean isActive;
}
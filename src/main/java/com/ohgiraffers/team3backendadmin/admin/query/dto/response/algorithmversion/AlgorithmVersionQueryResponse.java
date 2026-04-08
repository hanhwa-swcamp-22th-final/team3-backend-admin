package com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlgorithmVersionQueryResponse {

    private Long algorithmVersionId;
    private String versionNo;
    private String implementationKey;
    private String description;
    private Boolean isActive;
}
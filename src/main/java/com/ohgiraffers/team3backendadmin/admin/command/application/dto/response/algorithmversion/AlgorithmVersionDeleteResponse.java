package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmVersionDeleteResponse {

    private Long algorithmVersionId;
    private Boolean deleted;
}
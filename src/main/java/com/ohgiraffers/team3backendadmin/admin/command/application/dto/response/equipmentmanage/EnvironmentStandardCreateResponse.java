package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentStandardCreateResponse {
    private Long environmentStandardId;
    private EnvironmentType environmentType;
    private String environmentCode;
    private String environmentName;
}



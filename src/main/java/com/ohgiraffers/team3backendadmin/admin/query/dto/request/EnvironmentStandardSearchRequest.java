package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnvironmentStandardSearchRequest {
    private String keyword;
    private EnvironmentType environmentType;
}
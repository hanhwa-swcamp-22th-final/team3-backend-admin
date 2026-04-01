package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MaintenanceItemStandardSearchRequest {
    private String keyword;
}

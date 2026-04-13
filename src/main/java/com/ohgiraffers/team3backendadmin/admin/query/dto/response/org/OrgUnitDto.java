package com.ohgiraffers.team3backendadmin.admin.query.dto.response.org;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgUnitDto {
    private Long unitId;
    private String unitName;
    private String type;               // DEPARTMENT / TEAM
    private List<OrgUnitDto> children;
}

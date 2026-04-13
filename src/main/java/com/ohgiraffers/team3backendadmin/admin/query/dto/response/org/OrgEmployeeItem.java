package com.ohgiraffers.team3backendadmin.admin.query.dto.response.org;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrgEmployeeItem {
    private Long   employeeId;
    private String name;
    private String currentTier;
    private String role;
    private String departmentName;
    private String teamName;
}

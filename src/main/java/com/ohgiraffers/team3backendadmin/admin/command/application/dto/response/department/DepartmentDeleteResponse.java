package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDeleteResponse {
    private Long departmentId;
    private String departmentName;
}

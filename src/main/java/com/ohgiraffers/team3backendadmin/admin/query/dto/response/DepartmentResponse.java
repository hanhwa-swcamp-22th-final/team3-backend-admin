package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DepartmentResponse {

    private Long departmentId;
    private Long parentDepartmentId;
    private String departmentName;
    private String teamName;
    private String depth;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
}

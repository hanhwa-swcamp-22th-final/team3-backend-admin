package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DepartmentCreateRequest {

    private final Long parentDepartmentId;

    @NotBlank(message = "부서명은 필수 입력 항목입니다")
    private final String departmentName;

    private final String teamName;

    @NotBlank(message = "depth는 필수 입력 항목입니다")
    private final String depth;
}

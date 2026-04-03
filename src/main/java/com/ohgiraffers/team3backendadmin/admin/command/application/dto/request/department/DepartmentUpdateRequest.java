package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.department;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DepartmentUpdateRequest {

    @NotNull(message = "부서 ID는 필수 입력 항목입니다")
    private final Long departmentId;

    private final String departmentName;

    private final String teamName;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrgDepartmentLeaderRequest {

    @NotNull
    private Long employeeId;
}

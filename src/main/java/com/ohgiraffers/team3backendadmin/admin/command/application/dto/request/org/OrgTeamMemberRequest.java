package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrgTeamMemberRequest {
    private List<Long> employeeIds;
}

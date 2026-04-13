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
public class OrgTeamMembersDto {
    private OrgEmployeeItem leaderInfo;
    private List<OrgEmployeeItem> members;
}

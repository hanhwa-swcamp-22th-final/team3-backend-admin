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
public class OrgDepartmentDetailDto {

    private Long departmentId;
    private String departmentName;
    private int teamCount;
    private int totalMembers;
    private List<TeamSummary> teams;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamSummary {
        private Long teamId;
        private String teamName;
        private int memberCount;
    }
}

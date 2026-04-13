package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrgTeamRequest {
    private String teamName;
    private Long leaderId;
}

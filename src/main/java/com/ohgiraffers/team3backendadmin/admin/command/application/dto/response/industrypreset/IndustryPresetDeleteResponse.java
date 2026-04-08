package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustryPresetDeleteResponse {
    private Long configId;
    private String industryPresetName;
}

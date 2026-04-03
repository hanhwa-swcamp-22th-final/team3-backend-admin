package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IndustryPresetDeleteRequest {

    @NotNull(message = "설정 ID는 필수 입력 항목입니다")
    private final Long configId;

    @NotBlank(message = "산업 프리셋 이름은 필수 입력 항목입니다")
    private final String industryPresetName;
}

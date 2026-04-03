package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentProcessUpdateRequest {

    @NotNull(message = "생산 라인 ID는 필수입니다.")
    private Long factoryLineId;

    @NotBlank(message = "공정 코드는 필수입니다.")
    private String equipmentProcessCode;

    @NotBlank(message = "공정명은 필수입니다.")
    private String equipmentProcessName;
}

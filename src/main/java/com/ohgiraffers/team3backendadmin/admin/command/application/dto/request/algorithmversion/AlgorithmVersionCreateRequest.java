package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmVersionCreateRequest {

    @NotBlank(message = "버전 번호는 비어 있을 수 없습니다.")
    @Size(max = 50, message = "버전 번호는 50자 이하여야 합니다.")
    private String versionNo;

    @NotBlank(message = "구현 키는 비어 있을 수 없습니다.")
    @Size(max = 100, message = "구현 키는 100자 이하여야 합니다.")
    private String implementationKey;

    @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
    private String description;

    private String policyConfig;

    @NotNull(message = "활성 여부는 필수입니다.")
    private Boolean isActive;
}

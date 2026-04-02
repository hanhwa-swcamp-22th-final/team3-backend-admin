package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryLineCreateRequest {

    @NotBlank(message = "생산 라인 코드는 필수입니다.")
    private String factoryLineCode;

    @NotBlank(message = "생산 라인명은 필수입니다.")
    private String factoryLineName;
}

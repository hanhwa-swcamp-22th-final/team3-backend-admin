package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainKeywordCreateRequest {

    @NotBlank(message = "도메인 키워드는 비어 있을 수 없습니다.")
    @Size(min = 2, max = 50, message = "도메인 키워드는 2자 이상 50자 이하여야 합니다.")
    private String domainKeyword;

    private String domainKeywordDescription;

    @NotNull(message = "역량 카테고리는 필수입니다.")
    private DomainCompetencyCategory domainCompetencyCategory;

    @NotNull(message = "기본 점수는 필수입니다.")
    @DecimalMin(value = "0.1", message = "기본 점수는 0.1 이상이어야 합니다.")
    @DecimalMax(value = "10.0", message = "기본 점수는 10.0 이하여야 합니다.")
    private BigDecimal domainBaseScore;

    @NotNull(message = "가중치는 필수입니다.")
    @DecimalMin(value = "1.0", message = "가중치는 1.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "가중치는 5.0 이하여야 합니다.")
    private BigDecimal domainWeight;

    @NotNull(message = "활성 여부는 필수입니다.")
    private Boolean domainIsActive;
}

package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainKeywordUpdateResponse {

    private Long domainKeywordId;
    private String domainKeyword;
    private String domainKeywordDescription;
    private DomainCompetencyCategory domainCompetencyCategory;
    private BigDecimal domainBaseScore;
    private BigDecimal domainWeight;
    private Boolean domainIsActive;
}



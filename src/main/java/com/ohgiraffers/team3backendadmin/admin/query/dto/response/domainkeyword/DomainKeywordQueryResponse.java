package com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DomainKeywordQueryResponse {

    private Long domainKeywordId;
    private String domainKeyword;
    private String domainKeywordDescription;
    private DomainCompetencyCategory domainCompetencyCategory;
    private BigDecimal domainBaseScore;
    private BigDecimal domainWeight;
    private Boolean domainIsActive;
}



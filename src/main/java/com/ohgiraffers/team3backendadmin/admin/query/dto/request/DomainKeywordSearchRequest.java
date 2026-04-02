package com.ohgiraffers.team3backendadmin.admin.query.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainKeywordSearchRequest {

    private String keyword;
    private DomainCompetencyCategory category;
    private Boolean isActive;
}

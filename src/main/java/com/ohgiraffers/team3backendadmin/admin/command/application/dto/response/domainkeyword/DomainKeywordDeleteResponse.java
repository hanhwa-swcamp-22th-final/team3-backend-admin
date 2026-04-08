package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainKeywordDeleteResponse {

    private Long domainKeywordId;
    private Boolean deleted;
}



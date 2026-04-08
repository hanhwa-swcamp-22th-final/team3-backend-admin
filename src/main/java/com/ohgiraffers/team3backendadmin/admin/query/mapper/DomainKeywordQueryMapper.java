package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.domainkeyword.DomainKeywordSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordQueryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DomainKeywordQueryMapper {

    List<DomainKeywordQueryResponse> selectDomainKeywordList(DomainKeywordSearchRequest request);

    DomainKeywordDetailResponse selectDomainKeywordDetailById(Long domainKeywordId);
}

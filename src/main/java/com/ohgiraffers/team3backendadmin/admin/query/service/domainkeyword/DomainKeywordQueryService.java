package com.ohgiraffers.team3backendadmin.admin.query.service.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.DomainKeywordSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DomainKeywordQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DomainKeywordQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainKeywordQueryService {

    private final DomainKeywordQueryMapper domainKeywordQueryMapper;

    /**
     * 도메인 키워드 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 도메인 키워드 목록
     */
    public List<DomainKeywordQueryResponse> getDomainKeywordList(DomainKeywordSearchRequest request) {
        return domainKeywordQueryMapper.selectDomainKeywordList(request);
    }

    /**
     * 도메인 키워드 상세를 조회한다.
     * @param domainKeywordId 조회할 도메인 키워드 ID
     * @return 도메인 키워드 상세 응답
     */
    public DomainKeywordDetailResponse getDomainKeywordDetail(Long domainKeywordId) {
        DomainKeywordDetailResponse response = domainKeywordQueryMapper.selectDomainKeywordDetailById(domainKeywordId);
        if (response == null) {
            throw new BusinessException(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND);
        }
        return response;
    }
}

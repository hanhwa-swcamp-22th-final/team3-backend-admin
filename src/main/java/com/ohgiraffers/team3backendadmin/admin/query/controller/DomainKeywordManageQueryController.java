package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.domainkeyword.DomainKeywordSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.domainkeyword.DomainKeywordQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/domain-keyword")
public class DomainKeywordManageQueryController {

    private final DomainKeywordQueryService domainKeywordQueryService;

    /**
     * 도메인 키워드 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 도메인 키워드 목록 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DomainKeywordQueryResponse>>> getDomainKeywordList(
        DomainKeywordSearchRequest request
    ) {
        List<DomainKeywordQueryResponse> response = domainKeywordQueryService.getDomainKeywordList(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 도메인 키워드 상세 정보를 조회한다.
     * @param domainKeywordId 조회할 도메인 키워드 ID
     * @return 도메인 키워드 상세 응답
     */
    @GetMapping("/{domainKeywordId}")
    public ResponseEntity<ApiResponse<DomainKeywordDetailResponse>> getDomainKeywordDetail(
        @PathVariable Long domainKeywordId
    ) {
        DomainKeywordDetailResponse response = domainKeywordQueryService.getDomainKeywordDetail(domainKeywordId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

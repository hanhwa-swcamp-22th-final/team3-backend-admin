package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.FactoryLineQueryService;
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
@RequestMapping("/api/v1/factory-lines")
public class FactoryLineQueryController {

    private final FactoryLineQueryService factoryLineQueryService;

    /**
     * 검색 조건에 맞는 생산 라인 목록을 조회한다.
     * @param request 키워드 등 목록 조회 조건 값
     * @return 조회 조건에 맞는 생산 라인 목록을 담은 API 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FactoryLineQueryResponse>>> getFactoryLineList(FactoryLineSearchRequest request) {
        List<FactoryLineQueryResponse> factoryLineQueryResponses = factoryLineQueryService.getFactoryLineList(request);
        return ResponseEntity.ok(ApiResponse.success(factoryLineQueryResponses));
    }

    /**
     * 특정 생산 라인의 상세 정보를 조회한다.
     * @param factoryLineId 조회 대상 생산 라인의 식별자
     * @return 조회된 생산 라인 상세 정보를 담은 API 응답
     */
    @GetMapping("/{factoryLineId}")
    public ResponseEntity<ApiResponse<FactoryLineDetailResponse>> getFactoryLineDetail(@PathVariable Long factoryLineId) {
        FactoryLineDetailResponse factoryLineDetailResponse = factoryLineQueryService.getFactoryLineDetail(factoryLineId);
        return ResponseEntity.ok(ApiResponse.success(factoryLineDetailResponse));
    }
}

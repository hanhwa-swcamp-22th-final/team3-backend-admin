package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.algorithmversion.AlgorithmVersionSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.algorithmversion.AlgorithmVersionQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/algorithm-version")
public class AlgorithmVersionManageQueryController {

    private final AlgorithmVersionQueryService algorithmVersionQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlgorithmVersionQueryResponse>>> getAlgorithmVersionList(
        AlgorithmVersionSearchRequest request
    ) {
        List<AlgorithmVersionQueryResponse> response = algorithmVersionQueryService.getAlgorithmVersionList(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{algorithmVersionId}")
    public ResponseEntity<ApiResponse<AlgorithmVersionDetailResponse>> getAlgorithmVersionDetail(
        @PathVariable Long algorithmVersionId
    ) {
        AlgorithmVersionDetailResponse response = algorithmVersionQueryService
            .getAlgorithmVersionDetail(algorithmVersionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
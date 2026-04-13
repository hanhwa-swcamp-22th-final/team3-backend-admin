package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmPolicyReviewRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmVersionCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmVersionUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmPolicyReviewResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.algorithmversion.AlgorithmPolicyReviewService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.algorithmversion.AlgorithmVersionManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/algorithm-version")
public class AlgorithmVersionManageCommandController {

    private final AlgorithmVersionManageCommandService algorithmVersionManageCommandService;
    private final AlgorithmPolicyReviewService algorithmPolicyReviewService;


    @PostMapping("/policy-review")
    public ResponseEntity<ApiResponse<AlgorithmPolicyReviewResponse>> reviewPolicyConfig(
        @Valid @RequestBody AlgorithmPolicyReviewRequest request
    ) {
        AlgorithmPolicyReviewResponse response = algorithmPolicyReviewService.review(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<AlgorithmVersionCreateResponse>> createAlgorithmVersion(
        @Valid @RequestBody AlgorithmVersionCreateRequest request
    ) {
        AlgorithmVersionCreateResponse response = algorithmVersionManageCommandService.createAlgorithmVersion(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{algorithmVersionId}")
    public ResponseEntity<ApiResponse<AlgorithmVersionUpdateResponse>> updateAlgorithmVersion(
        @PathVariable Long algorithmVersionId,
        @Valid @RequestBody AlgorithmVersionUpdateRequest request
    ) {
        AlgorithmVersionUpdateResponse response = algorithmVersionManageCommandService.updateAlgorithmVersion(
            algorithmVersionId,
            request
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{algorithmVersionId}")
    public ResponseEntity<ApiResponse<AlgorithmVersionDeleteResponse>> deleteAlgorithmVersion(
        @PathVariable Long algorithmVersionId
    ) {
        AlgorithmVersionDeleteResponse response = algorithmVersionManageCommandService.deleteAlgorithmVersion(
            algorithmVersionId
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
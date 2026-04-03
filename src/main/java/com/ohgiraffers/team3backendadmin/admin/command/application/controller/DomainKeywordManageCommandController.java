package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DomainKeywordCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DomainKeywordUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.domainkeyword.DomainKeywordManageCommandService;
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
@RequestMapping("/api/v1/domain-keyword")
public class DomainKeywordManageCommandController {

    private final DomainKeywordManageCommandService domainKeywordManageCommandService;

    /**
     * 도메인 키워드를 생성한다.
     * @param request 생성할 도메인 키워드 정보
     * @return 생성된 도메인 키워드 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DomainKeywordCreateResponse>> createDomainKeyword(
        @Valid @RequestBody DomainKeywordCreateRequest request
    ) {
        DomainKeywordCreateResponse response = domainKeywordManageCommandService.createDomainKeyword(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 도메인 키워드를 수정한다.
     * @param domainKeywordId 수정할 도메인 키워드 ID
     * @param request 수정할 도메인 키워드 정보
     * @return 수정된 도메인 키워드 응답
     */
    @PutMapping("/{domainKeywordId}")
    public ResponseEntity<ApiResponse<DomainKeywordUpdateResponse>> updateDomainKeyword(
        @PathVariable Long domainKeywordId,
        @Valid @RequestBody DomainKeywordUpdateRequest request
    ) {
        DomainKeywordUpdateResponse response =
            domainKeywordManageCommandService.updateDomainKeyword(domainKeywordId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 도메인 키워드를 삭제한다.
     * @param domainKeywordId 삭제할 도메인 키워드 ID
     * @return 삭제 결과 응답
     */
    @DeleteMapping("/{domainKeywordId}")
    public ResponseEntity<ApiResponse<DomainKeywordDeleteResponse>> deleteDomainKeyword(
        @PathVariable Long domainKeywordId
    ) {
        DomainKeywordDeleteResponse response = domainKeywordManageCommandService.deleteDomainKeyword(domainKeywordId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

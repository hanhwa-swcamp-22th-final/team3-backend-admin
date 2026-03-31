package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.FactoryLineManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/factory-lines")

public class FactoryLineManageCommandController {

    private final FactoryLineManageCommandService factoryLineManageCommandService;



    /**
     * 관리자 화면에서 신규 생산 라인을 등록한다.
     * @param request 생산 라인 코드와 이름을 담은 생성 요청 값
     * @return 생성 완료된 생산 라인 정보를 담은 API 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FactoryLineCreateResponse>> createFactoryLine(
        @RequestBody FactoryLineCreateRequest request
    ) {
        FactoryLineCreateResponse factoryLineCreateResponse = factoryLineManageCommandService.createFactoryLine(request);
        return ResponseEntity.ok(ApiResponse.success(factoryLineCreateResponse));
    }

    /**
     * 메소드 의도
     * 식별자로 조회한 생산 라인의 코드와 이름을 수정한다,
     * @param factoryLineId 수정 대상 생산 라인의 식별자
     * @param request 수정할 생산 라인 코드와 이름 값
     * @return 수정 완료된 생산 라인 정보를 담은 API 응답
     */
    @PutMapping("/{factoryLineId}")
    public ResponseEntity<ApiResponse<FactoryLineUpdateResponse>> updateFactoryLine(
        @PathVariable Long factoryLineId,
        @RequestBody FactoryLineUpdateRequest request
    ) {
        FactoryLineUpdateResponse factoryLineUpdateResponse =
            factoryLineManageCommandService.updateFactoryLine(factoryLineId, request);
        return ResponseEntity.ok(ApiResponse.success(factoryLineUpdateResponse));
    }

    /**
     * 식별자로 조회한 생산 라인을 소프트 삭제 처리한다.
     * @param factoryLineId 삭제 대상 생산 라인의 식별자
     * @return 삭제 처리 후 생산 라인의 최신 상태를 담은 API 응답
     */
    @DeleteMapping("/{factoryLineId}")
    public ResponseEntity<ApiResponse<FactoryLineUpdateResponse>> deleteFactoryLine(@PathVariable Long factoryLineId) {
        FactoryLineUpdateResponse factoryLineUpdateResponse =
            factoryLineManageCommandService.deleteFactoryLine(factoryLineId);
        return ResponseEntity.ok(ApiResponse.success(factoryLineUpdateResponse));
    }
}

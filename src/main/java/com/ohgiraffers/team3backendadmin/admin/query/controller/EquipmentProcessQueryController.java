package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.EquipmentProcessQueryService;
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
@RequestMapping("/api/v1/equipment-processes")
public class EquipmentProcessQueryController {

    private final EquipmentProcessQueryService equipmentProcessQueryService;

    /**
     * 검색 조건에 맞는 공정 목록을 조회한다.
     * @param request 생산 라인 식별자와 키워드 등 목록 조회 조건 값
     * @return 조회 조건에 맞는 공정 목록을 담은 API 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipmentProcessQueryResponse>>> getEquipmentProcessList(EquipmentProcessSearchRequest request) {
        List<EquipmentProcessQueryResponse> equipmentProcessQueryResponses = equipmentProcessQueryService.getEquipmentProcessList(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessQueryResponses));
    }

    /**
     * 특정 공정의 상세 정보를 조회한다.
     * @param equipmentProcessId 조회 대상 공정의 식별자
     * @return 조회된 공정 상세 정보를 담은 API 응답
     */
    @GetMapping("/{equipmentProcessId}")
    public ResponseEntity<ApiResponse<EquipmentProcessDetailResponse>> getEquipmentProcessDetail(@PathVariable Long equipmentProcessId) {
        EquipmentProcessDetailResponse equipmentProcessDetailResponse = equipmentProcessQueryService.getEquipmentProcessDetail(equipmentProcessId);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessDetailResponse));
    }
}

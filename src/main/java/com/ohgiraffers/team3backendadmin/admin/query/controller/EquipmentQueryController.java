package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.EquipmentQueryService;
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
@RequestMapping("/api/v1/equipment")
public class EquipmentQueryController {

    private final EquipmentQueryService equipmentQueryService;

    /**
     * 검색 조건에 맞는 설비 목록을 조회한다.
     * @param request 공정, 상태, 등급, 키워드 등 목록 조회 조건 값
     * @return 조회 조건에 맞는 설비 목록을 담은 API 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipmentQueryResponse>>> getEquipmentList(EquipmentSearchRequest request) {
        List<EquipmentQueryResponse> equipmentQueryResponses = equipmentQueryService.getEquipmentList(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentQueryResponses));
    }

    /**
     * 특정 설비의 상세 정보를 조회한다.
     * @param equipmentId 조회 대상 설비의 식별자
     * @return 조회된 설비 상세 정보를 담은 API 응답
     */
    @GetMapping("/{equipmentId}")
    public ResponseEntity<ApiResponse<EquipmentDetailResponse>> getEquipmentDetail(@PathVariable Long equipmentId) {
        EquipmentDetailResponse equipmentDetailResponse = equipmentQueryService.getEquipmentDetail(equipmentId);
        return ResponseEntity.ok(ApiResponse.success(equipmentDetailResponse));
    }
}

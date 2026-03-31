package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
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
@RequestMapping("/api/v1/admin")
public class EquipmentManageQueryController {

    private final FactoryLineQueryService factoryLineQueryService;
    private final EquipmentProcessQueryService equipmentProcessQueryService;
    private final EquipmentQueryService equipmentQueryService;

    /**
     * 생산 라인 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 생산 라인 목록 응답
     */
    @GetMapping("/factory-lines")
    public ResponseEntity<ApiResponse<List<FactoryLineQueryResponse>>> getFactoryLineList(FactoryLineSearchRequest request) {
        List<FactoryLineQueryResponse> factoryLineQueryResponses = factoryLineQueryService.getFactoryLineList(request);
        return ResponseEntity.ok(ApiResponse.success(factoryLineQueryResponses));
    }

    /**
     * 생산 라인 상세 정보를 조회한다.
     * @param factoryLineId 조회할 생산 라인의 식별자
     * @return 생산 라인 상세 응답
     */
    @GetMapping("/factory-lines/{factoryLineId}")
    public ResponseEntity<ApiResponse<FactoryLineDetailResponse>> getFactoryLineDetail(@PathVariable Long factoryLineId) {
        FactoryLineDetailResponse factoryLineDetailResponse = factoryLineQueryService.getFactoryLineDetail(factoryLineId);
        return ResponseEntity.ok(ApiResponse.success(factoryLineDetailResponse));
    }

    /**
     * 공정 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 공정 목록 응답
     */
    @GetMapping("/equipment-processes")
    public ResponseEntity<ApiResponse<List<EquipmentProcessQueryResponse>>> getEquipmentProcessList(EquipmentProcessSearchRequest request) {
        List<EquipmentProcessQueryResponse> equipmentProcessQueryResponses = equipmentProcessQueryService.getEquipmentProcessList(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessQueryResponses));
    }

    /**
     * 공정 상세 정보를 조회한다.
     * @param equipmentProcessId 조회할 공정의 식별자
     * @return 공정 상세 응답
     */
    @GetMapping("/equipment-processes/{equipmentProcessId}")
    public ResponseEntity<ApiResponse<EquipmentProcessDetailResponse>> getEquipmentProcessDetail(@PathVariable Long equipmentProcessId) {
        EquipmentProcessDetailResponse equipmentProcessDetailResponse = equipmentProcessQueryService.getEquipmentProcessDetail(equipmentProcessId);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessDetailResponse));
    }

    /**
     * 설비 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 설비 목록 응답
     */
    @GetMapping("/equipments")
    public ResponseEntity<ApiResponse<List<EquipmentQueryResponse>>> getEquipmentList(EquipmentSearchRequest request) {
        List<EquipmentQueryResponse> equipmentQueryResponses = equipmentQueryService.getEquipmentList(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentQueryResponses));
    }

    /**
     * 설비 상세 정보를 조회한다.
     * @param equipmentId 조회할 설비의 식별자
     * @return 설비 상세 응답
     */
    @GetMapping("/equipments/{equipmentId}")
    public ResponseEntity<ApiResponse<EquipmentDetailResponse>> getEquipmentDetail(@PathVariable Long equipmentId) {
        EquipmentDetailResponse equipmentDetailResponse = equipmentQueryService.getEquipmentDetail(equipmentId);
        return ResponseEntity.ok(ApiResponse.success(equipmentDetailResponse));
    }
}
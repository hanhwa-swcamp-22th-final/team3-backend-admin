package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceItemStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceLogQueryService;
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
@RequestMapping("/api/v1/equipment-management")
public class EquipmentManageQueryController {

    private final FactoryLineQueryService factoryLineQueryService;
    private final EquipmentProcessQueryService equipmentProcessQueryService;
    private final EquipmentQueryService equipmentQueryService;
    private final EnvironmentStandardQueryService environmentStandardQueryService;
    private final EnvironmentEventQueryService environmentEventQueryService;
    private final MaintenanceItemStandardQueryService maintenanceItemStandardQueryService;
    private final MaintenanceLogQueryService maintenanceLogQueryService;

    /**
     * 생산 라인 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 생산 라인 목록 응답
     */
    @GetMapping("/factory-lines")
    public ResponseEntity<ApiResponse<List<FactoryLineQueryResponse>>> getFactoryLineList(FactoryLineSearchRequest request) {
        List<FactoryLineQueryResponse> responses = factoryLineQueryService.getFactoryLineList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 생산 라인 상세 정보를 조회한다.
     * @param factoryLineId 조회할 생산 라인 ID
     * @return 생산 라인 상세 응답
     */
    @GetMapping("/factory-lines/{factoryLineId}")
    public ResponseEntity<ApiResponse<FactoryLineDetailResponse>> getFactoryLineDetail(@PathVariable Long factoryLineId) {
        FactoryLineDetailResponse response = factoryLineQueryService.getFactoryLineDetail(factoryLineId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 공정 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 공정 목록 응답
     */
    @GetMapping("/equipment-processes")
    public ResponseEntity<ApiResponse<List<EquipmentProcessQueryResponse>>> getEquipmentProcessList(EquipmentProcessSearchRequest request) {
        List<EquipmentProcessQueryResponse> responses = equipmentProcessQueryService.getEquipmentProcessList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 공정 상세 정보를 조회한다.
     * @param equipmentProcessId 조회할 공정 ID
     * @return 공정 상세 응답
     */
    @GetMapping("/equipment-processes/{equipmentProcessId}")
    public ResponseEntity<ApiResponse<EquipmentProcessDetailResponse>> getEquipmentProcessDetail(@PathVariable Long equipmentProcessId) {
        EquipmentProcessDetailResponse response = equipmentProcessQueryService.getEquipmentProcessDetail(equipmentProcessId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 환경 기준 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 환경 기준 목록 응답
     */
    @GetMapping("/environment-standards")
    public ResponseEntity<ApiResponse<List<EnvironmentStandardQueryResponse>>> getEnvironmentStandardList(EnvironmentStandardSearchRequest request) {
        List<EnvironmentStandardQueryResponse> responses = environmentStandardQueryService.getEnvironmentStandardList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 환경 기준 상세 정보를 조회한다.
     * @param environmentStandardId 조회할 환경 기준 ID
     * @return 환경 기준 상세 응답
     */
    @GetMapping("/environment-standards/{environmentStandardId}")
    public ResponseEntity<ApiResponse<EnvironmentStandardDetailResponse>> getEnvironmentStandardDetail(@PathVariable Long environmentStandardId) {
        EnvironmentStandardDetailResponse response = environmentStandardQueryService.getEnvironmentStandardDetail(environmentStandardId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 환경 이벤트 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 환경 이벤트 목록 응답
     */
    @GetMapping("/environment-events")
    public ResponseEntity<ApiResponse<List<EnvironmentEventQueryResponse>>> getEnvironmentEventList(EnvironmentEventSearchRequest request) {
        List<EnvironmentEventQueryResponse> responses = environmentEventQueryService.getEnvironmentEventList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 환경 이벤트 상세 정보를 조회한다.
     * @param environmentEventId 조회할 환경 이벤트 ID
     * @return 환경 이벤트 상세 응답
     */
    @GetMapping("/environment-events/{environmentEventId}")
    public ResponseEntity<ApiResponse<EnvironmentEventDetailResponse>> getEnvironmentEventDetail(@PathVariable Long environmentEventId) {
        EnvironmentEventDetailResponse response = environmentEventQueryService.getEnvironmentEventDetail(environmentEventId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 유지보수 항목 기준 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 유지보수 항목 기준 목록 응답
     */
    @GetMapping("/maintenance-item-standards")
    public ResponseEntity<ApiResponse<List<MaintenanceItemStandardQueryResponse>>> getMaintenanceItemStandardList(
        MaintenanceItemStandardSearchRequest request
    ) {
        List<MaintenanceItemStandardQueryResponse> responses = maintenanceItemStandardQueryService.getMaintenanceItemStandardList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 유지보수 항목 기준 상세 정보를 조회한다.
     * @param maintenanceItemStandardId 조회할 유지보수 항목 기준 ID
     * @return 유지보수 항목 기준 상세 응답
     */
    @GetMapping("/maintenance-item-standards/{maintenanceItemStandardId}")
    public ResponseEntity<ApiResponse<MaintenanceItemStandardDetailResponse>> getMaintenanceItemStandardDetail(
        @PathVariable Long maintenanceItemStandardId
    ) {
        MaintenanceItemStandardDetailResponse response = maintenanceItemStandardQueryService.getMaintenanceItemStandardDetail(maintenanceItemStandardId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 유지보수 이력 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 유지보수 이력 목록 응답
     */
    @GetMapping("/maintenance-logs")
    public ResponseEntity<ApiResponse<List<MaintenanceLogQueryResponse>>> getMaintenanceLogList(MaintenanceLogSearchRequest request) {
        List<MaintenanceLogQueryResponse> responses = maintenanceLogQueryService.getMaintenanceLogList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 유지보수 이력 상세 정보를 조회한다.
     * @param maintenanceLogId 조회할 유지보수 이력 ID
     * @return 유지보수 이력 상세 응답
     */
    @GetMapping("/maintenance-logs/{maintenanceLogId}")
    public ResponseEntity<ApiResponse<MaintenanceLogDetailResponse>> getMaintenanceLogDetail(@PathVariable Long maintenanceLogId) {
        MaintenanceLogDetailResponse response = maintenanceLogQueryService.getMaintenanceLogDetail(maintenanceLogId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 설비 목록을 조회한다.
     * @param request 목록 조회 조건 정보
     * @return 설비 목록 응답
     */
    @GetMapping("/equipments")
    public ResponseEntity<ApiResponse<List<EquipmentQueryResponse>>> getEquipmentList(EquipmentSearchRequest request) {
        List<EquipmentQueryResponse> responses = equipmentQueryService.getEquipmentList(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * 설비 상세 정보를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 설비 상세 응답
     */
    @GetMapping("/equipments/{equipmentId}")
    public ResponseEntity<ApiResponse<EquipmentDetailResponse>> getEquipmentDetail(@PathVariable Long equipmentId) {
        EquipmentDetailResponse response = equipmentQueryService.getEquipmentDetail(equipmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

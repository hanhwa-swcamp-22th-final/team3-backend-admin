package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentAgingParamSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentBaselineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentAgingParamDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentEnumValuesResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentSummaryQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineEquipmentStatsResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentAgingParamQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentBaselineQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceItemStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceLogQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment-management")
public class EquipmentManageQueryController {

    private final FactoryLineQueryService factoryLineQueryService;
    private final EquipmentProcessQueryService equipmentProcessQueryService;
    private final EquipmentQueryService equipmentQueryService;
    private final EquipmentAgingParamQueryService equipmentAgingParamQueryService;
    private final EquipmentBaselineQueryService equipmentBaselineQueryService;
    private final EnvironmentStandardQueryService environmentStandardQueryService;
    private final EnvironmentEventQueryService environmentEventQueryService;
    private final MaintenanceItemStandardQueryService maintenanceItemStandardQueryService;
    private final MaintenanceLogQueryService maintenanceLogQueryService;

    @GetMapping("/enum-values")
    public ResponseEntity<ApiResponse<EquipmentEnumValuesResponse>> getEquipmentEnumValues() {
        EquipmentEnumValuesResponse response = equipmentQueryService.getEquipmentEnumValues();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

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
     * 생산 라인별 설비 통계 정보를 조회한다.
     * @param factoryLineId 조회할 생산 라인 ID
     * @return 생산 라인 설비 통계 응답
     */
    @GetMapping("/factory-lines/{factoryLineId}/equipment-stats")
    public ResponseEntity<ApiResponse<FactoryLineEquipmentStatsResponse>> getFactoryLineEquipmentStats(
        @PathVariable Long factoryLineId
    ) {
        FactoryLineEquipmentStatsResponse response = factoryLineQueryService.getFactoryLineEquipmentStats(factoryLineId);
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
     * 환경 이벤트를 조회한다.
     * history, latest, as-of, next, unresolved 모드를 query string으로 분기한다.
     * @param request 목록 조회 조건 정보
     * @param mode 조회 모드
     * @param equipmentId 설비 기준 조회 시 사용할 설비 ID
     * @param referenceTime 기준 시각 조회 시 사용할 시각
     * @return 환경 이벤트 목록 또는 상세 응답
     */
    @GetMapping("/environment-events")
    public ResponseEntity<ApiResponse<?>> getEnvironmentEventList(
        EnvironmentEventSearchRequest request,
        @RequestParam(value = "mode", required = false, defaultValue = "history") String mode,
        @RequestParam(required = false) Long equipmentId,
        @RequestParam(required = false) LocalDateTime referenceTime
    ) {
        if ("history".equals(mode)) {
            List<EnvironmentEventQueryResponse> responses = environmentEventQueryService.getEnvironmentEventList(request);
            return ResponseEntity.ok(ApiResponse.success(responses));
        }

        if ("latest".equals(mode)) {
            EnvironmentEventDetailResponse response = environmentEventQueryService.getLatestEnvironmentEvent(equipmentId);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("as-of".equals(mode)) {
            EnvironmentEventDetailResponse response =
                environmentEventQueryService.getLatestEnvironmentEventBeforeOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("next".equals(mode)) {
            EnvironmentEventDetailResponse response =
                environmentEventQueryService.getFirstEnvironmentEventAfterOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("unresolved".equals(mode)) {
            List<EnvironmentEventQueryResponse> responses =
                environmentEventQueryService.getUnresolvedEnvironmentEventList();
            return ResponseEntity.ok(ApiResponse.success(responses));
        }

        throw invalidMode("environment-events", mode);
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
     * 유지보수 이력을 조회한다.
     * history, latest, as-of, next, anomalies 모드를 query string으로 분기한다.
     * @param request 목록 조회 조건 정보
     * @param mode 조회 모드
     * @param equipmentId 설비 기준 조회 시 사용할 설비 ID
     * @param referenceDate 기준 일자 조회 시 사용할 일자
     * @return 유지보수 이력 목록 또는 상세 응답
     */
    @GetMapping("/maintenance-logs")
    public ResponseEntity<ApiResponse<?>> getMaintenanceLogList(
        MaintenanceLogSearchRequest request,
        @RequestParam(value = "mode", required = false, defaultValue = "history") String mode,
        @RequestParam(required = false) Long equipmentId,
        @RequestParam(required = false) LocalDate referenceDate
    ) {
        if ("history".equals(mode)) {
            List<MaintenanceLogQueryResponse> responses = maintenanceLogQueryService.getMaintenanceLogList(request);
            return ResponseEntity.ok(ApiResponse.success(responses));
        }

        if ("latest".equals(mode)) {
            MaintenanceLogDetailResponse response = maintenanceLogQueryService.getLatestMaintenanceLog(equipmentId);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("as-of".equals(mode)) {
            MaintenanceLogDetailResponse response =
                maintenanceLogQueryService.getLatestMaintenanceLogBeforeOrAt(equipmentId, referenceDate);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("next".equals(mode)) {
            MaintenanceLogDetailResponse response =
                maintenanceLogQueryService.getFirstMaintenanceLogAfterOrAt(equipmentId, referenceDate);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("anomalies".equals(mode)) {
            List<MaintenanceLogQueryResponse> responses =
                maintenanceLogQueryService.getAbnormalOrIncompleteMaintenanceLogList();
            return ResponseEntity.ok(ApiResponse.success(responses));
        }

        throw invalidMode("maintenance-logs", mode);
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
     * history 기본 목록과 summary, latest-snapshots 요약 목록 모드를 query string으로 분기한다.
     * @param request 목록 조회 조건 정보
     * @param mode 조회 모드
     * @return 설비 목록 응답
     */
    @GetMapping("/equipments")
    public ResponseEntity<ApiResponse<?>> getEquipmentList(
        EquipmentSearchRequest request,
        @RequestParam(value = "mode", required = false, defaultValue = "history") String mode
    ) {
        if ("summary".equals(mode)) {
            EquipmentSummaryQueryResponse response = equipmentQueryService.getEquipmentSummary();
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("latest-snapshots".equals(mode)) {
            List<EquipmentLatestSnapshotQueryResponse> responses = equipmentQueryService.getEquipmentListWithLatestSnapshots(request);
            return ResponseEntity.ok(ApiResponse.success(responses));
        }

        if (!"history".equals(mode)) {
            throw invalidMode("equipments", mode);
        }

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

    /**
     * 설비 노후 파라미터 이력 상세 정보를 조회한다.
     * @param equipmentAgingParamId 조회할 설비 노후 파라미터 이력 ID
     * @return 설비 노후 파라미터 상세 응답
     */
    @GetMapping("/equipment-aging-params/{equipmentAgingParamId}")
    public ResponseEntity<ApiResponse<EquipmentAgingParamDetailResponse>> getEquipmentAgingParamDetail(
        @PathVariable Long equipmentAgingParamId
    ) {
        EquipmentAgingParamDetailResponse response = equipmentAgingParamQueryService.getEquipmentAgingParamDetail(equipmentAgingParamId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/equipment-aging-params")
    /**
     * 설비 노후 파라미터 이력을 조회한다.
     * history, latest, as-of, next, uncalculated 모드를 query string으로 분기한다.
     * @param request 이력 조회 조건 정보
     * @param mode 조회 모드
     * @param equipmentId 설비 기준 조회 시 사용할 설비 ID
     * @param referenceTime 기준 시각 조회 시 사용할 시각
     * @return 설비 노후 파라미터 목록 또는 상세 응답
     */
    public ResponseEntity<ApiResponse<?>> getEquipmentAgingParamHistory(
        EquipmentAgingParamSearchRequest request,
        @RequestParam(value = "mode", required = false, defaultValue = "history") String mode,
        @RequestParam(required = false) Long equipmentId,
        @RequestParam(required = false) LocalDateTime referenceTime
    ) {
        if ("history".equals(mode)) {
            return ResponseEntity.ok(ApiResponse.success(
                equipmentAgingParamQueryService.getEquipmentAgingParamHistory(request)
            ));
        }

        if ("latest".equals(mode)) {
            EquipmentAgingParamDetailResponse response =
                equipmentAgingParamQueryService.getLatestEquipmentAgingParam(equipmentId);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("as-of".equals(mode)) {
            EquipmentAgingParamDetailResponse response =
                equipmentAgingParamQueryService.getLatestEquipmentAgingParamBeforeOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("next".equals(mode)) {
            EquipmentAgingParamDetailResponse response =
                equipmentAgingParamQueryService.getFirstEquipmentAgingParamAfterOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("uncalculated".equals(mode)) {
            return ResponseEntity.ok(ApiResponse.success(
                equipmentAgingParamQueryService.getUncalculatedEquipmentAgingParamList()
            ));
        }

        throw invalidMode("equipment-aging-params", mode);
    }

    /**
     * 설비 baseline 이력 상세 정보를 조회한다.
     * @param equipmentBaselineId 조회할 설비 baseline 이력 ID
     * @return 설비 baseline 상세 응답
     */
    @GetMapping("/equipment-baselines/{equipmentBaselineId}")
    public ResponseEntity<ApiResponse<EquipmentBaselineDetailResponse>> getEquipmentBaselineDetail(
        @PathVariable Long equipmentBaselineId
    ) {
        EquipmentBaselineDetailResponse response = equipmentBaselineQueryService.getEquipmentBaselineDetail(equipmentBaselineId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/equipment-baselines")
    /**
     * 설비 baseline 이력을 조회한다.
     * history, latest, as-of, next, uncalculated 모드를 query string으로 분기한다.
     * @param request 이력 조회 조건 정보
     * @param mode 조회 모드
     * @param equipmentId 설비 기준 조회 시 사용할 설비 ID
     * @param referenceTime 기준 시각 조회 시 사용할 시각
     * @return 설비 baseline 목록 또는 상세 응답
     */
    public ResponseEntity<ApiResponse<?>> getEquipmentBaselineHistory(
        EquipmentBaselineSearchRequest request,
        @RequestParam(value = "mode", required = false, defaultValue = "history") String mode,
        @RequestParam(required = false) Long equipmentId,
        @RequestParam(required = false) LocalDateTime referenceTime
    ) {
        if ("history".equals(mode)) {
            return ResponseEntity.ok(ApiResponse.success(
                equipmentBaselineQueryService.getEquipmentBaselineHistory(request)
            ));
        }

        if ("latest".equals(mode)) {
            EquipmentBaselineDetailResponse response =
                equipmentBaselineQueryService.getLatestEquipmentBaseline(equipmentId);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("as-of".equals(mode)) {
            EquipmentBaselineDetailResponse response =
                equipmentBaselineQueryService.getLatestEquipmentBaselineBeforeOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("next".equals(mode)) {
            EquipmentBaselineDetailResponse response =
                equipmentBaselineQueryService.getFirstEquipmentBaselineAfterOrAt(equipmentId, referenceTime);
            return ResponseEntity.ok(ApiResponse.success(response));
        }

        if ("uncalculated".equals(mode)) {
            return ResponseEntity.ok(ApiResponse.success(
                equipmentBaselineQueryService.getUncalculatedEquipmentBaselineList()
            ));
        }

        throw invalidMode("equipment-baselines", mode);
    }

    /**
     * 지원하지 않는 조회 모드 요청을 공통 형식으로 예외 처리한다.
     * @param resource 조회 대상 리소스명
     * @param mode 요청으로 전달된 조회 모드
     * @return 잘못된 모드 예외
     */
    private BusinessException invalidMode(String resource, String mode) {
        return new BusinessException(ErrorCode.INVALID_INPUT, "Unsupported mode for " + resource + ": " + mode);
    }
}

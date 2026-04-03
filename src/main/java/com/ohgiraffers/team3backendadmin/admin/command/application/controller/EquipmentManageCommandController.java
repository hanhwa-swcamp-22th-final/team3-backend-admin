package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentAgingParamUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentBaselineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentAgingParamUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentBaselineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentEventManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentStandardManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentAgingParamManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentBaselineManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.MaintenanceItemStandardManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.MaintenanceLogManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment-management")
public class EquipmentManageCommandController {

  private final FactoryLineManageCommandService factoryLineManageCommandService;
  private final EquipmentProcessManageCommandService equipmentProcessManageCommandService;
  private final EquipmentManageCommandService equipmentManageCommandService;
  private final EquipmentAgingParamManageCommandService equipmentAgingParamManageCommandService;
  private final EquipmentBaselineManageCommandService equipmentBaselineManageCommandService;
  private final EnvironmentStandardManageCommandService environmentStandardManageCommandService;
  private final EnvironmentEventManageCommandService environmentEventManageCommandService;
  private final MaintenanceItemStandardManageCommandService maintenanceItemStandardManageCommandService;
  private final MaintenanceLogManageCommandService maintenanceLogManageCommandService;

  /**
   * 생산 라인을 생성한다.
   * @param request 생성할 생산 라인 요청 정보
   * @return 생성된 생산 라인 정보를 담은 응답
   */
  @PostMapping("/factory-lines")
  public ResponseEntity<ApiResponse<FactoryLineCreateResponse>> createFactoryLine(
      @RequestBody FactoryLineCreateRequest request
  ) {
    FactoryLineCreateResponse response = factoryLineManageCommandService.createFactoryLine(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 생산 라인 정보를 수정한다.
   * @param factoryLineId 수정할 생산 라인 식별자
   * @param request 수정할 생산 라인 요청 정보
   * @return 수정된 생산 라인 정보를 담은 응답
   */
  @PutMapping("/factory-lines/{factoryLineId}")
  public ResponseEntity<ApiResponse<FactoryLineUpdateResponse>> updateFactoryLine(
      @PathVariable Long factoryLineId,
      @RequestBody FactoryLineUpdateRequest request
  ) {
    FactoryLineUpdateResponse response = factoryLineManageCommandService.updateFactoryLine(factoryLineId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 생산 라인을 소프트 삭제한다.
   * @param factoryLineId 삭제할 생산 라인 식별자
   * @return 삭제 처리된 생산 라인 정보를 담은 응답
   */
  @DeleteMapping("/factory-lines/{factoryLineId}")
  public ResponseEntity<ApiResponse<FactoryLineUpdateResponse>> deleteFactoryLine(@PathVariable Long factoryLineId) {
    FactoryLineUpdateResponse response = factoryLineManageCommandService.deleteFactoryLine(factoryLineId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 공정을 생성한다.
   * @param request 생성할 공정 요청 정보
   * @return 생성된 공정 정보를 담은 응답
   */
  @PostMapping("/equipment-processes")
  public ResponseEntity<ApiResponse<EquipmentProcessCreateResponse>> createEquipmentProcess(
      @RequestBody EquipmentProcessCreateRequest request
  ) {
    EquipmentProcessCreateResponse response = equipmentProcessManageCommandService.createEquipmentProcess(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 공정 정보를 수정한다.
   * @param equipmentProcessId 수정할 공정 식별자
   * @param request 수정할 공정 요청 정보
   * @return 수정된 공정 정보를 담은 응답
   */
  @PutMapping("/equipment-processes/{equipmentProcessId}")
  public ResponseEntity<ApiResponse<EquipmentProcessUpdateResponse>> updateEquipmentProcess(
      @PathVariable Long equipmentProcessId,
      @RequestBody EquipmentProcessUpdateRequest request
  ) {
    EquipmentProcessUpdateResponse response =
        equipmentProcessManageCommandService.updateEquipmentProcess(equipmentProcessId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 공정을 소프트 삭제한다.
   * @param equipmentProcessId 삭제할 공정 식별자
   * @return 삭제 처리된 공정 정보를 담은 응답
   */
  @DeleteMapping("/equipment-processes/{equipmentProcessId}")
  public ResponseEntity<ApiResponse<EquipmentProcessUpdateResponse>> deleteEquipmentProcess(
      @PathVariable Long equipmentProcessId
  ) {
    EquipmentProcessUpdateResponse response = equipmentProcessManageCommandService.deleteEquipmentProcess(equipmentProcessId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 기준을 생성한다.
   * @param request 생성할 환경 기준 요청 정보
   * @return 생성된 환경 기준 정보를 담은 응답
   */
  @PostMapping("/environment-standards")
  public ResponseEntity<ApiResponse<EnvironmentStandardCreateResponse>> createEnvironmentStandard(
      @RequestBody EnvironmentStandardCreateRequest request
  ) {
    EnvironmentStandardCreateResponse response = environmentStandardManageCommandService.createEnvironmentStandard(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 기준 정보를 수정한다.
   * @param environmentStandardId 수정할 환경 기준 식별자
   * @param request 수정할 환경 기준 요청 정보
   * @return 수정된 환경 기준 정보를 담은 응답
   */
  @PutMapping("/environment-standards/{environmentStandardId}")
  public ResponseEntity<ApiResponse<EnvironmentStandardUpdateResponse>> updateEnvironmentStandard(
      @PathVariable Long environmentStandardId,
      @RequestBody EnvironmentStandardUpdateRequest request
  ) {
    EnvironmentStandardUpdateResponse response =
        environmentStandardManageCommandService.updateEnvironmentStandard(environmentStandardId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 기준을 소프트 삭제한다.
   * @param environmentStandardId 삭제할 환경 기준 식별자
   * @return 삭제 처리된 환경 기준 정보를 담은 응답
   */
  @DeleteMapping("/environment-standards/{environmentStandardId}")
  public ResponseEntity<ApiResponse<EnvironmentStandardUpdateResponse>> deleteEnvironmentStandard(
      @PathVariable Long environmentStandardId
  ) {
    EnvironmentStandardUpdateResponse response =
        environmentStandardManageCommandService.deleteEnvironmentStandard(environmentStandardId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 이벤트를 생성한다.
   * @param request 생성할 환경 이벤트 요청 정보
   * @return 생성된 환경 이벤트 정보를 담은 응답
   */
  @PostMapping("/environment-events")
  public ResponseEntity<ApiResponse<EnvironmentEventCreateResponse>> createEnvironmentEvent(
      @RequestBody EnvironmentEventCreateRequest request
  ) {
    EnvironmentEventCreateResponse response = environmentEventManageCommandService.createEnvironmentEvent(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 이벤트 정보를 수정한다.
   * @param environmentEventId 수정할 환경 이벤트 식별자
   * @param request 수정할 환경 이벤트 요청 정보
   * @return 수정된 환경 이벤트 정보를 담은 응답
   */
  @PutMapping("/environment-events/{environmentEventId}")
  public ResponseEntity<ApiResponse<EnvironmentEventUpdateResponse>> updateEnvironmentEvent(
      @PathVariable Long environmentEventId,
      @RequestBody EnvironmentEventUpdateRequest request
  ) {
    EnvironmentEventUpdateResponse response =
        environmentEventManageCommandService.updateEnvironmentEvent(environmentEventId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 환경 이벤트를 삭제한다.
   * @param environmentEventId 삭제할 환경 이벤트 식별자
   * @return 삭제 처리된 환경 이벤트 정보를 담은 응답
   */
  @DeleteMapping("/environment-events/{environmentEventId}")
  public ResponseEntity<ApiResponse<EnvironmentEventUpdateResponse>> deleteEnvironmentEvent(
      @PathVariable Long environmentEventId
  ) {
    EnvironmentEventUpdateResponse response =
        environmentEventManageCommandService.deleteEnvironmentEvent(environmentEventId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 항목 기준을 생성한다.
   * @param request 생성할 유지보수 항목 기준 정보
   * @return 생성된 유지보수 항목 기준 응답
   */
  @PostMapping("/maintenance-item-standards")
  public ResponseEntity<ApiResponse<MaintenanceItemStandardCreateResponse>> createMaintenanceItemStandard(
      @RequestBody MaintenanceItemStandardCreateRequest request
  ) {
    MaintenanceItemStandardCreateResponse response =
        maintenanceItemStandardManageCommandService.createMaintenanceItemStandard(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 항목 기준 정보를 수정한다.
   * @param maintenanceItemStandardId 수정할 유지보수 항목 기준 ID
   * @param request 수정할 유지보수 항목 기준 정보
   * @return 수정된 유지보수 항목 기준 응답
   */
  @PutMapping("/maintenance-item-standards/{maintenanceItemStandardId}")
  public ResponseEntity<ApiResponse<MaintenanceItemStandardUpdateResponse>> updateMaintenanceItemStandard(
      @PathVariable Long maintenanceItemStandardId,
      @RequestBody MaintenanceItemStandardUpdateRequest request
  ) {
    MaintenanceItemStandardUpdateResponse response =
        maintenanceItemStandardManageCommandService.updateMaintenanceItemStandard(maintenanceItemStandardId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 항목 기준을 삭제한다.
   * @param maintenanceItemStandardId 삭제할 유지보수 항목 기준 ID
   * @return 삭제된 유지보수 항목 기준 응답
   */
  @DeleteMapping("/maintenance-item-standards/{maintenanceItemStandardId}")
  public ResponseEntity<ApiResponse<MaintenanceItemStandardUpdateResponse>> deleteMaintenanceItemStandard(
      @PathVariable Long maintenanceItemStandardId
  ) {
    MaintenanceItemStandardUpdateResponse response =
        maintenanceItemStandardManageCommandService.deleteMaintenanceItemStandard(maintenanceItemStandardId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 이력을 생성한다.
   * @param request 생성할 유지보수 이력 정보
   * @return 생성된 유지보수 이력 응답
   */
  @PostMapping("/maintenance-logs")
  public ResponseEntity<ApiResponse<MaintenanceLogCreateResponse>> createMaintenanceLog(
      @RequestBody MaintenanceLogCreateRequest request
  ) {
    MaintenanceLogCreateResponse response = maintenanceLogManageCommandService.createMaintenanceLog(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 이력 정보를 수정한다.
   * @param maintenanceLogId 수정할 유지보수 이력 ID
   * @param request 수정할 유지보수 이력 정보
   * @return 수정된 유지보수 이력 응답
   */
  @PutMapping("/maintenance-logs/{maintenanceLogId}")
  public ResponseEntity<ApiResponse<MaintenanceLogUpdateResponse>> updateMaintenanceLog(
      @PathVariable Long maintenanceLogId,
      @RequestBody MaintenanceLogUpdateRequest request
  ) {
    MaintenanceLogUpdateResponse response =
        maintenanceLogManageCommandService.updateMaintenanceLog(maintenanceLogId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 유지보수 이력을 삭제한다.
   * @param maintenanceLogId 삭제할 유지보수 이력 ID
   * @return 삭제된 유지보수 이력 응답
   */
  @DeleteMapping("/maintenance-logs/{maintenanceLogId}")
  public ResponseEntity<ApiResponse<MaintenanceLogUpdateResponse>> deleteMaintenanceLog(
      @PathVariable Long maintenanceLogId
  ) {
    MaintenanceLogUpdateResponse response =
        maintenanceLogManageCommandService.deleteMaintenanceLog(maintenanceLogId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 설비를 생성한다.
   * @param request 생성할 설비 요청 정보
   * @return 생성된 설비 정보를 담은 응답
   */
  @PostMapping("/equipments")
  public ResponseEntity<ApiResponse<EquipmentCreateResponse>> createEquipment(
      @RequestBody EquipmentCreateRequest request
  ) {
    EquipmentCreateResponse response = equipmentManageCommandService.createEquipment(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 설비 정보를 수정한다.
   * @param equipmentId 수정할 설비 식별자
   * @param request 수정할 설비 요청 정보
   * @return 수정 처리 결과를 담은 응답
   */
  @PutMapping("/equipments/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> updateEquipment(
      @PathVariable Long equipmentId,
      @RequestBody EquipmentUpdateRequest request
  ) {
    equipmentManageCommandService.updateEquipment(equipmentId, request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  /**
   * 설비를 삭제한다.
   * @param equipmentId 삭제할 설비 식별자
   * @return 삭제 처리 결과를 담은 응답
   */
  @DeleteMapping("/equipments/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long equipmentId) {
    equipmentManageCommandService.deleteEquipment(equipmentId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  /**
   * 설비 노후 파라미터 이력 정보를 수정한다.
   * @param equipmentAgingParamId 수정할 설비 노후 파라미터 이력 식별자
   * @param request 수정할 설비 노후 파라미터 요청 정보
   * @return 수정된 설비 노후 파라미터 정보를 담은 응답
   */
  @PutMapping("/equipment-aging-params/{equipmentAgingParamId}")
  public ResponseEntity<ApiResponse<EquipmentAgingParamUpdateResponse>> updateEquipmentAgingParam(
      @PathVariable Long equipmentAgingParamId,
      @RequestBody EquipmentAgingParamUpdateRequest request
  ) {
    EquipmentAgingParamUpdateResponse response =
        equipmentAgingParamManageCommandService.updateEquipmentAgingParam(equipmentAgingParamId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 설비 baseline 이력 정보를 수정한다.
   * @param equipmentBaselineId 수정할 설비 baseline 이력 식별자
   * @param request 수정할 설비 baseline 요청 정보
   * @return 수정된 설비 baseline 정보를 담은 응답
   */
  @PutMapping("/equipment-baselines/{equipmentBaselineId}")
  public ResponseEntity<ApiResponse<EquipmentBaselineUpdateResponse>> updateEquipmentBaseline(
      @PathVariable Long equipmentBaselineId,
      @RequestBody EquipmentBaselineUpdateRequest request
  ) {
    EquipmentBaselineUpdateResponse response =
        equipmentBaselineManageCommandService.updateEquipmentBaseline(equipmentBaselineId, request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}

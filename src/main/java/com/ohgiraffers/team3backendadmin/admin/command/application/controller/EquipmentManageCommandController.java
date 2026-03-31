package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
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
@RequestMapping("/api/v1/equipment-management")
public class EquipmentManageCommandController {

  private final FactoryLineManageCommandService factoryLineManageCommandService;
  private final EquipmentProcessManageCommandService equipmentProcessManageCommandService;
  private final EquipmentManageCommandService equipmentManageCommandService;

  /**
   * 생산 라인을 생성한다.
   * @param request 생성할 생산 라인의 요청 정보
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
   * @param factoryLineId 수정할 생산 라인의 식별자
   * @param request 수정할 생산 라인의 요청 정보
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
   * @param factoryLineId 삭제할 생산 라인의 식별자
   * @return 삭제 처리된 생산 라인 정보를 담은 응답
   */
  @DeleteMapping("/factory-lines/{factoryLineId}")
  public ResponseEntity<ApiResponse<FactoryLineUpdateResponse>> deleteFactoryLine(@PathVariable Long factoryLineId) {
    FactoryLineUpdateResponse response = factoryLineManageCommandService.deleteFactoryLine(factoryLineId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 공정을 생성한다.
   * @param request 생성할 공정의 요청 정보
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
   * @param equipmentProcessId 수정할 공정의 식별자
   * @param request 수정할 공정의 요청 정보
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
   * @param equipmentProcessId 삭제할 공정의 식별자
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
   * 설비를 생성한다.
   * @param request 생성할 설비의 요청 정보
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
   * @param equipmentId 수정할 설비의 식별자
   * @param request 수정할 설비의 요청 정보
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
   * @param equipmentId 삭제할 설비의 식별자
   * @return 삭제 처리 결과를 담은 응답
   */
  @DeleteMapping("/equipments/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long equipmentId) {
    equipmentManageCommandService.deleteEquipment(equipmentId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
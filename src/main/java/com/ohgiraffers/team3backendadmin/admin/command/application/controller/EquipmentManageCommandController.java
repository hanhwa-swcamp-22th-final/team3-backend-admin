package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.EquipmentManageCommandService;
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
@RequestMapping("/api/v1/equipment")
public class EquipmentManageCommandController {

  private final EquipmentManageCommandService equipmentManageCommandService;

  /**
   * 공정과 환경 기준이 연결된 신규 설비를 등록한다.
   * @param request 설비 기본 정보와 노후화 파라미터 값을 담은 생성 요청 값
   * @return 생성 완료된 설비 정보를 담은 API 응답
   */
  @PostMapping
  public ResponseEntity<ApiResponse<EquipmentCreateResponse>> createEquipment(
      @RequestBody EquipmentCreateRequest request
  ) {
    EquipmentCreateResponse response = equipmentManageCommandService.createEquipment(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /**
   * 기존 설비와 연관 노후화 파라미터를 함께 수정한다.
   * @param equipmentId 수정 대상 설비의 식별자
   * @param request 수정할 설비 정보와 노후화 파라미터 값
   * @return 수정 완료 응답
   */
  @PutMapping("/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> updateEquipment(
      @PathVariable Long equipmentId,
      @RequestBody EquipmentUpdateRequest request
  ) {
    equipmentManageCommandService.updateEquipment(equipmentId, request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  /**
   * 설비와 연관된 기준 데이터를 함께 제거한다.
   * @param equipmentId 삭제 대상 설비의 식별자
   * @return 삭제 완료 응답
   */
  @DeleteMapping("/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long equipmentId) {
    equipmentManageCommandService.deleteEquipment(equipmentId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}

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

  @PostMapping
  public ResponseEntity<ApiResponse<EquipmentCreateResponse>> createEquipment(
      @RequestBody EquipmentCreateRequest request
  ) {
    EquipmentCreateResponse response = equipmentManageCommandService.createEquipment(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PutMapping("/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> updateEquipment(
      @PathVariable Long equipmentId,
      @RequestBody EquipmentUpdateRequest request
  ) {
    equipmentManageCommandService.updateEquipment(equipmentId, request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{equipmentId}")
  public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable Long equipmentId) {
    equipmentManageCommandService.deleteEquipment(equipmentId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
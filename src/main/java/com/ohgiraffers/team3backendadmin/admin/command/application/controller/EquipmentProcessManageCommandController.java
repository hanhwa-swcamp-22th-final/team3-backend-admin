package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.EquipmentProcessManageCommandService;
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
@RequestMapping("/api/v1/equipment-processes")
public class EquipmentProcessManageCommandController {

    private final EquipmentProcessManageCommandService equipmentProcessManageCommandService;

    /**
     * 특정 생산 라인에 속한 신규 공정을 등록한다.
     * @param request 생산 라인 식별자와 공정 코드, 공정명을 담은 생성 요청 값
     * @return 생성 완료된 공정 정보를 담은 API 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EquipmentProcessCreateResponse>> createEquipmentProcess(
        @RequestBody EquipmentProcessCreateRequest request
    ) {
        EquipmentProcessCreateResponse equipmentProcessCreateResponse =
            equipmentProcessManageCommandService.createEquipmentProcess(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessCreateResponse));
    }

    /**
     * 식별자로 조회한 공정의 소속 라인과 기본 정보를 수정한다.
     * @param equipmentProcessId 수정 대상 공정의 식별자
     * @param request 수정할 생산 라인 식별자, 공정 코드, 공정명 값
     * @return 수정 완료된 공정 정보를 담은 API 응답
     */
    @PutMapping("/{equipmentProcessId}")
    public ResponseEntity<ApiResponse<EquipmentProcessUpdateResponse>> updateEquipmentProcess(
        @PathVariable Long equipmentProcessId,
        @RequestBody EquipmentProcessUpdateRequest request
    ) {
        EquipmentProcessUpdateResponse equipmentProcessUpdateResponse =
            equipmentProcessManageCommandService.updateEquipmentProcess(equipmentProcessId, request);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessUpdateResponse));
    }

    /**
     * 식별자로 조회한 공정을 소프트 삭제 처리한다.
     * @param equipmentProcessId 삭제 대상 공정의 식별자
     * @return 삭제 처리 후 공정의 최신 상태를 담은 API 응답
     */
    @DeleteMapping("/{equipmentProcessId}")
    public ResponseEntity<ApiResponse<EquipmentProcessUpdateResponse>> deleteEquipmentProcess(
        @PathVariable Long equipmentProcessId
    ) {
        EquipmentProcessUpdateResponse equipmentProcessUpdateResponse =
            equipmentProcessManageCommandService.deleteEquipmentProcess(equipmentProcessId);
        return ResponseEntity.ok(ApiResponse.success(equipmentProcessUpdateResponse));
    }
}

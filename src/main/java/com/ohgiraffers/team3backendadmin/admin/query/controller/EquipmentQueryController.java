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

    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipmentQueryResponse>>> getEquipmentList(EquipmentSearchRequest request) {
        List<EquipmentQueryResponse> equipmentQueryResponses = equipmentQueryService.getEquipmentList(request);
        return ResponseEntity.ok(ApiResponse.success(equipmentQueryResponses));
    }

    @GetMapping("/{equipmentId}")
    public ResponseEntity<ApiResponse<EquipmentDetailResponse>> getEquipmentDetail(@PathVariable Long equipmentId) {
        EquipmentDetailResponse equipmentDetailResponse = equipmentQueryService.getEquipmentDetail(equipmentId);
        return ResponseEntity.ok(ApiResponse.success(equipmentDetailResponse));
    }
}

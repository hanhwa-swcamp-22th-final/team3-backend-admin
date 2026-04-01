package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.OCSAWeightConfigResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.industrypreset.IndustryPresetQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/industry")
public class IndustryPresetQueryController {

    private final IndustryPresetQueryService industryPresetQueryService;

    @GetMapping("/presets")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<OCSAWeightConfigResponse>>> getAllPresets() {
        List<OCSAWeightConfigResponse> presets = industryPresetQueryService.getAllPresets();
        return ResponseEntity.ok(ApiResponse.success(presets));
    }
}

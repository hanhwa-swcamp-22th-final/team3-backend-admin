package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.industrypreset.IndustryPresetCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/industry-presets")
public class IndustryPresetCommandController {

    private final IndustryPresetCommandService industryPresetCommandService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> create(
            @Valid @RequestBody IndustryPresetCreateRequest request
    ) {
        industryPresetCommandService.create(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

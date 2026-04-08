package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetDeleteRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.industrypreset.IndustryPresetCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/industry")
public class IndustryPresetCommandController {

    private final IndustryPresetCommandService industryPresetCommandService;

    @PostMapping("/preset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<IndustryPresetCreateResponse>> create(
            @Valid @RequestBody IndustryPresetCreateRequest request
    ) {
        IndustryPresetCreateResponse response = industryPresetCommandService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/preset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<IndustryPresetUpdateResponse>> update(
            @Valid @RequestBody IndustryPresetUpdateRequest request
    ) {
        IndustryPresetUpdateResponse response = industryPresetCommandService.update(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/preset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<IndustryPresetDeleteResponse>> delete(
            @Valid @RequestBody IndustryPresetDeleteRequest request
    ) {
        IndustryPresetDeleteResponse response = industryPresetCommandService.delete(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

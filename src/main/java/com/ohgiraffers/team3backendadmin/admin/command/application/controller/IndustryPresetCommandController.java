package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetDeleteRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetUpdateRequest;
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
    public ResponseEntity<ApiResponse<Void>> create(
            @Valid @RequestBody IndustryPresetCreateRequest request
    ) {
        industryPresetCommandService.create(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/preset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> update(
            @Valid @RequestBody IndustryPresetUpdateRequest request
    ) {
        industryPresetCommandService.update(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/preset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Valid @RequestBody IndustryPresetDeleteRequest request
    ) {
        industryPresetCommandService.delete(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

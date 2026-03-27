package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.OrganizationManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization")
public class OrganizationManageController {

    private final OrganizationManageCommandService organizationManageCommandService;

    @PostMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> insertDepartment(
            @Valid @RequestBody DepartmentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        organizationManageCommandService.insertDepartment(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PutMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateDepartment(
            @Valid @RequestBody DepartmentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        organizationManageCommandService.updateDepartment(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long departmentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        organizationManageCommandService.deleteDepartment(departmentId, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeTierUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.EmployeeManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/employees")
public class AdminEmployeeCommandController {

    private final EmployeeManageCommandService employeeManageCommandService;

    @PatchMapping("/{employeeId}/tier")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM')")
    public ResponseEntity<ApiResponse<Void>> updateEmployeeTier(
        @PathVariable Long employeeId,
        @RequestBody EmployeeTierUpdateRequest request
    ) {
        employeeManageCommandService.updateEmployeeTier(employeeId, request.getTier());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

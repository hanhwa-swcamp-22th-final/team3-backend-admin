package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement.EmployeeHrQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/employees")
public class EmployeeManageQueryController {

    private final EmployeeHrQueryService employeeHrQueryService;

    @GetMapping("/{employeeId}/profile")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL', 'WORKER')")
    public ResponseEntity<ApiResponse<EmployeeProfileQueryResponse>> getWorkerProfile(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getProfile(employeeId)));
    }

    @GetMapping("/{employeeId}/skills")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL', 'WORKER')")
    public ResponseEntity<ApiResponse<List<EmployeeSkillQueryResponse>>> getWorkerSkills(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getSkills(employeeId)));
    }

    @GetMapping("/{employeeId}/tier-chart")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL', 'WORKER')")
    public ResponseEntity<ApiResponse<List<TierChartPointQueryResponse>>> getTierChart(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getTierChart(employeeId)));
    }

    @GetMapping("/{leaderId}/team-members")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL')")
    public ResponseEntity<ApiResponse<List<Long>>> getTeamMemberIds(@PathVariable Long leaderId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getTeamMemberIds(leaderId)));
    }

    @GetMapping("/workers/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM')")
    public ResponseEntity<ApiResponse<List<Long>>> getActiveWorkerIdsByTier(@RequestParam EmployeeTier tier) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getActiveWorkerIdsByTier(tier)));
    }

    @GetMapping("/workers/active-by-department")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL', 'WORKER')")
    public ResponseEntity<ApiResponse<List<Long>>> getActiveWorkerIdsByDepartmentId(@RequestParam Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getActiveWorkerIdsByDepartmentId(departmentId)));
    }

    @GetMapping("/workers/active-by-root-department")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL')")
    public ResponseEntity<ApiResponse<List<Long>>> getActiveWorkerIdsByRootDepartmentId(@RequestParam Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getActiveWorkerIdsByRootDepartmentId(departmentId)));
    }

    @GetMapping("/{employeeId}/active-worker")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM')")
    public ResponseEntity<ApiResponse<Boolean>> existsActiveWorkerByIdAndTier(
            @PathVariable Long employeeId,
            @RequestParam EmployeeTier tier
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                employeeHrQueryService.existsActiveWorkerByIdAndTier(employeeId, tier)
        ));
    }

    @PostMapping("/profiles/batch")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM', 'DL', 'TL', 'WORKER')")
    public ResponseEntity<ApiResponse<List<EmployeeProfileQueryResponse>>> getProfilesBatch(
            @RequestBody List<Long> ids
    ) {
        return ResponseEntity.ok(ApiResponse.success(employeeHrQueryService.getProfiles(ids)));
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentLeaderRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamMemberRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.org.AdminOrgCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 조직도 관련 Admin-side REST 컨트롤러.
 * 기본 경로: /api/v1/admin/org
 * <p>
 * HR 백엔드의 Feign 클라이언트가 호출하는 조직 변경 API를 제공한다.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/admin/org")
@RequiredArgsConstructor
public class AdminOrgController {

    private final AdminOrgCommandService orgCommandService;

    /** HR-076: 신규 부서 생성 */
    @PostMapping("/departments")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<Long>> createDepartment(
            @RequestBody OrgDepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(orgCommandService.createDepartment(request)));
    }

    /** HR-077: 부서 수정 */
    @PutMapping("/departments/{departmentId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<Long>> updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody OrgDepartmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                orgCommandService.updateDepartment(departmentId, request)));
    }

    /** HR-078: 부서 삭제 */
    @DeleteMapping("/departments/{departmentId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        orgCommandService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    /** HR-079: 신규 팀 생성 */
    @PostMapping("/departments/{departmentId}/teams")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<Long>> createTeam(
            @PathVariable Long departmentId,
            @RequestBody OrgTeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(orgCommandService.createTeam(departmentId, request)));
    }

    /** HR-080: 팀 수정 */
    @PutMapping("/teams/{teamId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<Void> updateTeam(
            @PathVariable Long teamId,
            @RequestBody OrgTeamRequest request) {
        orgCommandService.updateTeam(teamId, request);
        return ResponseEntity.noContent().build();
    }

    /** HR-081: 팀 삭제 */
    @DeleteMapping("/teams/{teamId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        orgCommandService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    /** HR-083: 팀원 추가 */
    @PostMapping("/teams/{teamId}/members")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<Void> addTeamMembers(
            @PathVariable Long teamId,
            @RequestBody OrgTeamMemberRequest request) {
        orgCommandService.addTeamMembers(teamId, request);
        return ResponseEntity.noContent().build();
    }

    /** HR-084: 팀원 제거 */
    @DeleteMapping("/teams/{teamId}/members/{employeeId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<Void> removeTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long employeeId) {
        orgCommandService.removeTeamMember(teamId, employeeId);
        return ResponseEntity.noContent().build();
    }

    /** HR-085: 부서장 지정 */
    @RequestMapping(
            value = "/departments/{departmentId}/leader",
            method = {RequestMethod.PATCH, RequestMethod.PUT}
    )
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<Long>> assignDepartmentLeader(
            @PathVariable Long departmentId,
            @Valid @RequestBody OrgDepartmentLeaderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                orgCommandService.assignDepartmentLeader(departmentId, request)));
    }
}

package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgDepartmentDetailDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgEmployeeItem;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgTeamMembersDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgUnitDto;
import com.ohgiraffers.team3backendadmin.admin.query.service.org.AdminOrgQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 조직도 관련 Admin-side 조회 API.
 * HR 백엔드의 Feign 클라이언트가 호출하며, URL은 기존 command 컨트롤러와 동일하게 유지한다.
 */
@RestController
@RequestMapping("/api/v1/admin/org")
@RequiredArgsConstructor
public class AdminOrgQueryController {

    private final AdminOrgQueryService orgQueryService;

    /** HR-073: 조직도 트리 조회 */
    @GetMapping("/units")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrgUnitDto>> getOrgTree() {
        return ResponseEntity.ok(ApiResponse.success(orgQueryService.getOrgTree()));
    }

    /** HR-074: 직원 목록 조회 (필터·페이징) */
    @GetMapping("/employees")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<OrgEmployeeItem>>> getEmployees(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                orgQueryService.getEmployees(departmentId, teamId, keyword, page, size)));
    }

    /** HR-075: 팀원 목록 및 팀장 정보 조회 */
    @GetMapping("/teams/{teamId}/members")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrgTeamMembersDto>> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.success(orgQueryService.getTeamMembers(teamId)));
    }

    /** HR-082: 부서 상세 조회 */
    @GetMapping("/departments/{departmentId}")
    @PreAuthorize("hasAnyAuthority('HRM', 'ADMIN')")
    public ResponseEntity<ApiResponse<OrgDepartmentDetailDto>> getDepartmentDetail(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(ApiResponse.success(orgQueryService.getDepartmentDetail(departmentId)));
    }
}

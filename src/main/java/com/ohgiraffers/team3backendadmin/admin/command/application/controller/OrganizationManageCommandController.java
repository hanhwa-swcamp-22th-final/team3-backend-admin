package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeRoleChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeSkillUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.DepartmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.EmployeeManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.EmployeeRoleManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.EmployeeSkillManageCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 직원/부서 관리 페이지 컨트롤러. (조직관리)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization")
public class OrganizationManageCommandController {

    private final DepartmentManageCommandService departmentManageCommandService;
    private final EmployeeManageCommandService employeeManageCommandService;
    private final EmployeeSkillManageCommandService employeeSkillManageCommandService;
    private final EmployeeRoleManageCommandService employeeRoleManageCommandService;

    /**
     * 새로운 부서를 추가하는 Api
     * @param request DepartmentCreateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PostMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> insertDepartment(
            @Valid @RequestBody DepartmentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        departmentManageCommandService.insertDepartment(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    /**
     * 부서의 정보를 수정하는 Api
     * @param request DepartmentUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PutMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateDepartment(
            @Valid @RequestBody DepartmentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        departmentManageCommandService.updateDepartment(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 부서정보를 삭제(Soft Delete)하는 Api
     * @param departmentId departmentId
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @DeleteMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long departmentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        departmentManageCommandService.deleteDepartment(departmentId, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 새로운 Employee를 추가하는 Api
     * @param request EmployeeCreateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> insertEmployee(
            @Valid @RequestBody EmployeeCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        employeeManageCommandService.insertEmployee(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    /**
     * 사원의 개인정보를 수정하는 Api
     * @param request EmployeeUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PutMapping("/employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(
            @Valid @RequestBody EmployeeUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        employeeManageCommandService.updateEmployee(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 사원을 삭제(Soft Delete)하는 Api
     * @param employeeCode targetCode
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @DeleteMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(
            @PathVariable String employeeCode,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        employeeManageCommandService.deleteEmployee(employeeCode, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 사원의 스킬 점수를 수정하는 Api
     * @param request EmployeeSkillUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PutMapping("/employee/skill")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateEmployeeSkill(
            @Valid @RequestBody EmployeeSkillUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        employeeSkillManageCommandService.updateEmployeeSkill(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 사원의 역할(Role)을 변경하는 Api
     * @param request EmployeeRoleChangeRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<>>
     */
    @PutMapping("/employee/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> changeEmployeeRole(
            @Valid @RequestBody EmployeeRoleChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        employeeRoleManageCommandService.changeEmployeeRole(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

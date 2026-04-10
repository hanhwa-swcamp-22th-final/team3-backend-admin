package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.department.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.department.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeDepartmentMatchRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeRoleChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeSkillUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeDepartmentMatchResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeRoleChangeResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeSkillUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeUpdateResponse;
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
     * @return ResponseEntity<ApiResponse<DepartmentCreateResponse>>
     */
    @PostMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentCreateResponse>> insertDepartment(
            @Valid @RequestBody DepartmentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        DepartmentCreateResponse response = departmentManageCommandService.insertDepartment(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 부서의 정보를 수정하는 Api
     * @param request DepartmentUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<DepartmentUpdateResponse>>
     */
    @PutMapping("/department")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentUpdateResponse>> updateDepartment(
            @Valid @RequestBody DepartmentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        DepartmentUpdateResponse response = departmentManageCommandService.updateDepartment(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 부서정보를 삭제(Soft Delete)하는 Api
     * @param departmentId departmentId
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<DepartmentDeleteResponse>>
     */
    @DeleteMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDeleteResponse>> deleteDepartment(
            @PathVariable Long departmentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        DepartmentDeleteResponse response = departmentManageCommandService.deleteDepartment(departmentId, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 새로운 Employee를 추가하는 Api
     * @param request EmployeeCreateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeCreateResponse>>
     */
    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeCreateResponse>> insertEmployee(
            @Valid @RequestBody EmployeeCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeCreateResponse response = employeeManageCommandService.insertEmployee(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 사원을 삭제(Soft Delete)하는 Api
     * @param employeeCode targetCode
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeDeleteResponse>>
     */
    @DeleteMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeDeleteResponse>> deleteEmployee(
            @PathVariable String employeeCode,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeDeleteResponse response = employeeManageCommandService.deleteEmployee(employeeCode, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 사원의 스킬 점수를 수정하는 Api
     * @param request EmployeeSkillUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeSkillUpdateResponse>>
     */
    @PutMapping("/employee/skill")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeSkillUpdateResponse>> updateEmployeeSkill(
            @Valid @RequestBody EmployeeSkillUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeSkillUpdateResponse response = employeeSkillManageCommandService.updateEmployeeSkill(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 사원의 역할(Role)을 변경하는 Api
     * @param request EmployeeRoleChangeRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeRoleChangeResponse>>
     */
    @PutMapping("/employee/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeRoleChangeResponse>> changeEmployeeRole(
            @Valid @RequestBody EmployeeRoleChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeRoleChangeResponse response = employeeRoleManageCommandService.changeEmployeeRole(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 부서에 사원을 배치하는 Api
     * @param request EmployeeDepartmentMatchRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeDepartmentMatchResponse>>
     */
    @PutMapping("/employee/department")
    @PreAuthorize("hasAuthority('HRM')")
    public ResponseEntity<ApiResponse<EmployeeDepartmentMatchResponse>> matchDepartment(
            @Valid @RequestBody EmployeeDepartmentMatchRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeDepartmentMatchResponse response = employeeManageCommandService.matchDepartment(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 관리자가 사원 정보를 업데이트하는 Api
     * @param request EmployeeUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<EmployeeUpdateResponse>>
     */
    @PutMapping("/employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeUpdateResponse>> updateEmployee(
            @Valid @RequestBody EmployeeUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        EmployeeUpdateResponse response = employeeManageCommandService.updateEmployee(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

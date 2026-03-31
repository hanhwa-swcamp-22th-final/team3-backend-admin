package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.OrganizationManageQueryService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization")
public class OrganizationManageQueryController {

    private final OrganizationManageQueryService organizationManageQueryService;

    /**
     * 선택 부서 조회
     * @param departmentId 부서ID
     * @return ResponseEntity<ApiResponse<DepartmentResponse>>
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(
            @PathVariable Long departmentId
    ) {
        DepartmentResponse department = organizationManageQueryService.getDepartmentById(departmentId);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    /**
     * 모든 부서 조회
     * @return ResponseEntity<ApiResponse<List<DepartmentResponse>>>
     */
    @GetMapping("/departments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = organizationManageQueryService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    /**
     * 선택 사원 조회
     * @param employeeCode 사원코드
     * @return ResponseEntity<ApiResponse<EmployeeResponse>>
     */
    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(
            @PathVariable String employeeCode
    ) {
        EmployeeResponse employee = organizationManageQueryService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    /**
     * 모든 사원 조회
     * @return ResponseEntity<ApiResponse<List<EmployeeResponse>>>
     */
    @GetMapping("/employees")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> employees = organizationManageQueryService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
}

package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSummaryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement.DepartmentManageQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement.EmployeeManageQueryService;
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

    private final DepartmentManageQueryService departmentManageQueryService;
    private final EmployeeManageQueryService employeeManageQueryService;

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
        DepartmentResponse department = departmentManageQueryService.getDepartmentById(departmentId);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    /**
     * 모든 부서 조회
     * @return ResponseEntity<ApiResponse<List<DepartmentResponse>>>
     */
    @GetMapping("/departments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentManageQueryService.getAllDepartments();
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
        EmployeeResponse employee = employeeManageQueryService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }

    /**
     * 모든 사원 조회
     * @return ResponseEntity<ApiResponse<List<EmployeeResponse>>>
     */
    @GetMapping("/employees")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HRM')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeManageQueryService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    /**
     * 사원 요약 목록 조회 (이름, 코드, 등급, 입사일, 라인명, 설비명)
     * @return ResponseEntity<ApiResponse<List<EmployeeSummaryResponse>>>
     */
    @GetMapping("/employees/summary")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<EmployeeSummaryResponse>>> getEmployeeSummaries() {
        List<EmployeeSummaryResponse> summaries = employeeManageQueryService.getAllEmployeeSummaries();
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }
}

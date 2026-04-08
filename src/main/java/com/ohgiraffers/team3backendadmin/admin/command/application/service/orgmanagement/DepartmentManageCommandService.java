package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.department.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.department.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.department.DepartmentUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentManageCommandService {

    private final OrganizationManageDomainService organizationManageDomainService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final IdGenerator idGenerator;

    // Insert Department
    @Transactional
    public DepartmentCreateResponse insertDepartment(DepartmentCreateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Department department = Department.builder()
                .departmentId(idGenerator.generate())
                .parentDepartmentId(request.getParentDepartmentId())
                .departmentName(request.getDepartmentName())
                .teamName(request.getTeamName())
                .depth(request.getDepth())
                .build();

        Department verified = organizationManageDomainService.buildVerifiedDepartment(department);

        departmentRepository.save(verified);

        return DepartmentCreateResponse.builder()
                .parentDepartmentId(verified.getParentDepartmentId())
                .departmentName(verified.getDepartmentName())
                .teamName(verified.getTeamName())
                .depth(verified.getDepth())
                .build();
    }

    // Update Department
    @Transactional
    public DepartmentUpdateResponse updateDepartment(DepartmentUpdateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        department.updateNames(
                request.getDepartmentName(),
                request.getTeamName()
        );

        return DepartmentUpdateResponse.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .teamName(department.getTeamName())
                .build();
    }

    // Delete Department
    @Transactional
    public DepartmentDeleteResponse deleteDepartment(Long departmentId, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        DepartmentDeleteResponse response = DepartmentDeleteResponse.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .build();

        department.softDelete();

        return response;
    }
}

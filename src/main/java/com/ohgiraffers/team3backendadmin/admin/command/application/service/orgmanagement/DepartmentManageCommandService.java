package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
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
    public void insertDepartment(DepartmentCreateRequest request, String employeeCode) {

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
    }

    // Update Department
    @Transactional
    public void updateDepartment(DepartmentUpdateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        department.updateNames(
                request.getDepartmentName(),
                request.getTeamName()
        );
    }

    // Delete Department
    @Transactional
    public void deleteDepartment(Long departmentId, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        department.softDelete();
    }
}

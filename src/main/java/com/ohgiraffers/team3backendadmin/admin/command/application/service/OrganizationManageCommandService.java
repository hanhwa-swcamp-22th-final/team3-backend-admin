package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationManageCommandService {

    private final OrganizationManageDomainService organizationManageDomainService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    // Insert Department
    @Transactional
    public void insertDepartment(DepartmentCreateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new BadCredentialsException("해당 사원 정보를 찾을 수 없습니다"));

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
                .orElseThrow(() -> new BadCredentialsException("해당 사원 정보를 찾을 수 없습니다"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다"));

        department.updateNames(
                request.getDepartmentName(),
                request.getTeamName()
        );
    }

    // Delete Department
    @Transactional
    public void deleteDepartment(Long departmentId, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new BadCredentialsException("해당 사원 정보를 찾을 수 없습니다"));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다"));

        department.softDelete();
    }

    // Insert Employee
    @Transactional
    public void insertEmployee(EmployeeCreateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new BadCredentialsException("해당 사원 정보를 찾을 수 없습니다"));

        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다"));

        String generatedCode = organizationManageDomainService.generateEmployeeCode();

        Employee employee = Employee.builder()
                .employeeId(idGenerator.generate())
                .departmentId(request.getDepartmentId())
                .employeeCode(generatedCode)
                .employeeName(request.getEmployeeName())
                .employeeEmail(request.getEmployeeEmail())
                .employeePhone(passwordEncoder.encode(request.getEmployeePhone()))
                .employeeAddress(passwordEncoder.encode(request.getEmployeeAddress()))
                .employeeEmergencyContact(passwordEncoder.encode(request.getEmployeeEmergencyContact()))
                .employeePassword(passwordEncoder.encode(request.getEmployeePassword()))
                .employeeRole(request.getEmployeeRole())
                .employeeStatus(request.getEmployeeStatus())
                .employeeTier(request.getEmployeeTier())
                .build();

        employeeRepository.save(employee);
    }

    // Update employee

    // Delete employee

}

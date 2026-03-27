package com.ohgiraffers.team3backendadmin.admin.command.domain.service;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrganizationManageDomainService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 부서 등록 시 depth, teamName을 검증/결정한다.
     * - 루트 부서 (parentDepartmentId == null): depth = "L0", teamName = null
     * - 하위 부서: depth = 상위 부서 depth + 1, teamName 유지
     */
    public Department buildVerifiedDepartment(Department department) {
        Long parentDepartmentId = department.getParentDepartmentId();

        if (parentDepartmentId == null) {
            return Department.builder()
                    .departmentId(department.getDepartmentId())
                    .parentDepartmentId(null)
                    .departmentName(department.getDepartmentName())
                    .teamName(null)
                    .depth("L0")
                    .build();
        }

        Department parentDepartment = departmentRepository.findById(parentDepartmentId)
                .orElseThrow(() -> new IllegalArgumentException("상위 부서를 찾을 수 없습니다"));

        int parentLevel = Integer.parseInt(parentDepartment.getDepth().substring(1));
        String depth = "L" + (parentLevel + 1);

        return Department.builder()
                .departmentId(department.getDepartmentId())
                .parentDepartmentId(parentDepartmentId)
                .departmentName(department.getDepartmentName())
                .teamName(department.getTeamName())
                .depth(depth)
                .build();
    }

    public String generateEmployeeCode() {
        String prefix = "EMP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

        return employeeRepository.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix)
                .map(employee -> {
                    String lastCode = employee.getEmployeeCode();
                    int lastNum = Integer.parseInt(lastCode.substring(prefix.length()));
                    return prefix + String.format("%03d", lastNum + 1);
                })
                .orElse(prefix + "001");
    }
}

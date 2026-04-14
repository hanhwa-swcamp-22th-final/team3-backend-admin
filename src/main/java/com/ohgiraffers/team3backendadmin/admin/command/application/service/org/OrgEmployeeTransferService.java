package com.ohgiraffers.team3backendadmin.admin.command.application.service.org;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrgEmployeeTransferService {

    private static final List<EmployeeRole> LEADER_ROLES = List.of(EmployeeRole.TL, EmployeeRole.DL);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public Employee transfer(Long employeeId, Long targetDepartmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        Department targetDepartment = departmentRepository.findById(targetDepartmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        if (!isLeader(employee)) {
            employee.assignDepartment(targetDepartmentId);
            return employee;
        }

        if (isDeleted(targetDepartment)) {
            employee.assignDepartment(targetDepartmentId);
            return employee;
        }

        if (employeeRepository.existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(
                targetDepartmentId,
                LEADER_ROLES,
                employee.getEmployeeId()
        )) {
            throw new InvalidInputException("해당 부서에 이미 부서장이 존재하여 발령할 수 없습니다.");
        }

        employee.updateRole(resolveLeaderRole(targetDepartment));
        employee.assignDepartment(targetDepartmentId);
        return employee;
    }

    private boolean isLeader(Employee employee) {
        return employee.getEmployeeRole() == EmployeeRole.TL || employee.getEmployeeRole() == EmployeeRole.DL;
    }

    private boolean isDeleted(Department department) {
        return Boolean.TRUE.equals(department.getIsDeleted());
    }

    private EmployeeRole resolveLeaderRole(Department department) {
        return isTopDepartment(department) ? EmployeeRole.DL : EmployeeRole.TL;
    }

    private boolean isTopDepartment(Department department) {
        String depth = department.getDepth();
        if (depth == null || depth.isBlank()) {
            return department.getParentDepartmentId() == null;
        }
        return "L0".equalsIgnoreCase(depth) || "DEPARTMENT".equalsIgnoreCase(depth);
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaEmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeCode(String employeeCode);
    Optional<Employee> findByEmployeeEmail(String employeeEmail);
    Optional<Employee> findByEmployeePhone(String employeePhone);
    boolean existsByEmployeeEmail(String employeeEmail);
    boolean existsByEmployeePhone(String employeePhone);
    boolean existsByEmployeeEmailAndEmployeeIdNot(String employeeEmail, Long employeeId);
    boolean existsByEmployeePhoneAndEmployeeIdNot(String employeePhone, Long employeeId);
    Optional<Employee> findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(String prefix);
    List<Employee> findByDepartmentIdAndEmployeeRole(Long departmentId, EmployeeRole employeeRole);
    boolean existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(
        Long departmentId,
        Collection<EmployeeRole> employeeRoles,
        Long employeeId
    );
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByDepartmentIdIn(Collection<Long> departmentIds);
}

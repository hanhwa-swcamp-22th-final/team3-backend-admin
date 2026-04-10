package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByParentDepartmentIdAndIsDeletedFalse(Long parentDepartmentId);

    List<Department> findByParentDepartmentIdAndIsDeletedFalse(Long parentDepartmentId);
}

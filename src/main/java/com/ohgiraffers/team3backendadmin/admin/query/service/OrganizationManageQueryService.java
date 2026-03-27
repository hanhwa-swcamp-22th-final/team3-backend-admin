package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationManageQueryService {

    private final DepartmentMapper departmentMapper;

    public DepartmentResponse getDepartmentById(Long departmentId) {
        DepartmentResponse department = departmentMapper.findById(departmentId);
        if (department == null) {
            throw new IllegalArgumentException("해당 부서를 찾을 수 없습니다");
        }
        return department;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentMapper.findAll();
    }
}

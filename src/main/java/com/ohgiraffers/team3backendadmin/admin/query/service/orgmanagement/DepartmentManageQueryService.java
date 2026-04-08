package com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentManageQueryService {

    private final DepartmentMapper departmentMapper;

    public DepartmentResponse getDepartmentById(Long departmentId) {
        DepartmentResponse department = departmentMapper.findById(departmentId);
        if (department == null) {
            throw new DepartmentNotFoundException();
        }
        return department;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentMapper.findAll();
    }
}

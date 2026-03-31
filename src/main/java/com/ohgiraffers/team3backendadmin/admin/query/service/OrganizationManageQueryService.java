package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EmployeeMapper;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationManageQueryService {

    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;
    private final AesEncryptor aesEncryptor;

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

    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        EmployeeResponse employee = employeeMapper.findByEmployeeCode(employeeCode);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return decryptEmployeeInfo(employee);
    }

    public List<EmployeeResponse> getAllEmployees() {
        List<EmployeeResponse> employees = employeeMapper.findAll();
        employees.forEach(this::decryptEmployeeInfo);
        return employees;
    }

    private EmployeeResponse decryptEmployeeInfo(EmployeeResponse employee) {
        employee.setEmployeeEmail(aesEncryptor.decrypt(employee.getEmployeeEmail()));
        employee.setEmployeePhone(aesEncryptor.decrypt(employee.getEmployeePhone()));
        employee.setEmployeeAddress(aesEncryptor.decrypt(employee.getEmployeeAddress()));
        employee.setEmployeeEmergencyContact(aesEncryptor.decrypt(employee.getEmployeeEmergencyContact()));
        return employee;
    }
}

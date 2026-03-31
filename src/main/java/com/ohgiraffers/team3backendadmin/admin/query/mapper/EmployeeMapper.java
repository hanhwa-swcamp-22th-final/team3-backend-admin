package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeResponse findByEmployeeCode(String employeeCode);
    List<EmployeeResponse> findAll();
}

package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSummaryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeResponse findByEmployeeCode(String employeeCode);
    List<EmployeeResponse> findAll();
    List<EmployeeSummaryResponse> findAllSummary();
    EmployeeProfileQueryResponse findProfileByEmployeeId(Long employeeId);
    List<EmployeeSkillQueryResponse> findSkillsByEmployeeId(Long employeeId);
    List<TierChartPointQueryResponse> findTierChartByEmployeeId(Long employeeId);
}

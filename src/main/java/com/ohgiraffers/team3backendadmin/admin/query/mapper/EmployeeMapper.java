package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeSummaryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeResponse findByEmployeeCode(String employeeCode);
    List<EmployeeResponse> findAll();
    List<EmployeeSummaryResponse> findAllSummary();
    EmployeeProfileQueryResponse findProfileByEmployeeId(Long employeeId);
    List<EmployeeSkillQueryResponse> findSkillsByEmployeeId(Long employeeId);
    List<TierChartPointQueryResponse> findTierChartByEmployeeId(Long employeeId);
    List<Long> findTeamMemberIdsByLeaderId(@Param("leaderId") Long leaderId);
    List<Long> findActiveWorkerIdsByTier(@Param("tier") String tier);
    boolean existsActiveWorkerByIdAndTier(@Param("employeeId") Long employeeId, @Param("tier") String tier);
}

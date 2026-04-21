package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSummaryResponse;
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
    List<Long> findActiveWorkerIdsByTier(@Param("tier") EmployeeTier tier);
    List<Long> findActiveWorkerIdsByDepartmentId(@Param("departmentId") Long departmentId);
    List<Long> findActiveWorkerIdsByRootDepartmentId(@Param("departmentId") Long departmentId);
    boolean existsActiveWorkerByIdAndTier(@Param("employeeId") Long employeeId, @Param("tier") EmployeeTier tier);

    List<EmployeeProfileQueryResponse> findProfilesByIds(@Param("ids") List<Long> ids);
}

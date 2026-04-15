package com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EmployeeMapper;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeHrQueryService {

    private final EmployeeMapper employeeMapper;

    public EmployeeProfileQueryResponse getProfile(Long employeeId) {
        EmployeeProfileQueryResponse profile = employeeMapper.findProfileByEmployeeId(employeeId);
        if (profile == null) {
            throw new EmployeeNotFoundException();
        }
        return profile;
    }

    public List<EmployeeSkillQueryResponse> getSkills(Long employeeId) {
        return employeeMapper.findSkillsByEmployeeId(employeeId);
    }

    public List<TierChartPointQueryResponse> getTierChart(Long employeeId) {
        return employeeMapper.findTierChartByEmployeeId(employeeId);
    }

    public List<Long> getTeamMemberIds(Long leaderId) {
        EmployeeProfileQueryResponse profile = employeeMapper.findProfileByEmployeeId(leaderId);
        if (profile == null) {
            throw new EmployeeNotFoundException();
        }
        return employeeMapper.findTeamMemberIdsByLeaderId(leaderId);
    }

    public List<Long> getActiveWorkerIdsByTier(EmployeeTier tier) {
        return employeeMapper.findActiveWorkerIdsByTier(tier);
    }

    public boolean existsActiveWorkerByIdAndTier(Long employeeId, EmployeeTier tier) {
        return employeeMapper.existsActiveWorkerByIdAndTier(employeeId, tier);
    }
}

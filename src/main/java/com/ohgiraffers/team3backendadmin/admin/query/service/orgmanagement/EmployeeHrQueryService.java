package com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaDepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EmployeeMapper;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.config.security.CustomUserDetails;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeHrQueryService {

    private final EmployeeMapper employeeMapper;
    private final JpaEmployeeRepository employeeRepository;
    private final JpaDepartmentRepository departmentRepository;

    public EmployeeProfileQueryResponse getProfile(CustomUserDetails userDetails, Long employeeId) {
        validateEmployeeAccess(loadRequester(userDetails), employeeId);
        EmployeeProfileQueryResponse profile = employeeMapper.findProfileByEmployeeId(employeeId);
        if (profile == null) {
            throw new EmployeeNotFoundException();
        }
        return profile;
    }

    public List<EmployeeSkillQueryResponse> getSkills(CustomUserDetails userDetails, Long employeeId) {
        validateEmployeeAccess(loadRequester(userDetails), employeeId);
        return employeeMapper.findSkillsByEmployeeId(employeeId);
    }

    public List<TierChartPointQueryResponse> getTierChart(CustomUserDetails userDetails, Long employeeId) {
        validateEmployeeAccess(loadRequester(userDetails), employeeId);
        return employeeMapper.findTierChartByEmployeeId(employeeId);
    }

    public List<Long> getTeamMemberIds(CustomUserDetails userDetails, Long leaderId) {
        validateLeaderScope(loadRequester(userDetails), leaderId);
        EmployeeProfileQueryResponse profile = employeeMapper.findProfileByEmployeeId(leaderId);
        if (profile == null) {
            throw new EmployeeNotFoundException();
        }
        return employeeMapper.findTeamMemberIdsByLeaderId(leaderId);
    }

    public List<Long> getActiveWorkerIdsByTier(EmployeeTier tier) {
        return employeeMapper.findActiveWorkerIdsByTier(tier);
    }

    public List<Long> getActiveWorkerIdsByDepartmentId(CustomUserDetails userDetails, Long departmentId) {
        validateDepartmentScope(loadRequester(userDetails), departmentId);
        return employeeMapper.findActiveWorkerIdsByDepartmentId(departmentId);
    }

    public List<Long> getActiveWorkerIdsByRootDepartmentId(CustomUserDetails userDetails, Long departmentId) {
        validateDepartmentScope(loadRequester(userDetails), departmentId);
        return employeeMapper.findActiveWorkerIdsByRootDepartmentId(departmentId);
    }

    public boolean existsActiveWorkerByIdAndTier(Long employeeId, EmployeeTier tier) {
        return employeeMapper.existsActiveWorkerByIdAndTier(employeeId, tier);
    }

    public List<EmployeeProfileQueryResponse> getProfiles(CustomUserDetails userDetails, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        validateEmployeeBatchAccess(loadRequester(userDetails), ids);
        return employeeMapper.findProfilesByIds(ids);
    }

    private Employee loadRequester(CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getEmployeeId() == null) {
            throw new AdminAccessDeniedException();
        }
        return employeeRepository.findById(userDetails.getEmployeeId())
                .orElseThrow(AdminAccessDeniedException::new);
    }

    private void validateEmployeeAccess(Employee requester, Long targetEmployeeId) {
        if (targetEmployeeId == null) {
            throw new AdminAccessDeniedException();
        }
        if (hasFullEmployeeScope(requester)) {
            return;
        }
        if (requester.getEmployeeRole() == EmployeeRole.WORKER) {
            if (!requester.getEmployeeId().equals(targetEmployeeId)) {
                throw new AdminAccessDeniedException();
            }
            return;
        }
        Set<Long> allowedIds = getAllowedEmployeeIds(requester);
        allowedIds.add(requester.getEmployeeId());
        if (!allowedIds.contains(targetEmployeeId)) {
            throw new AdminAccessDeniedException();
        }
    }

    private void validateEmployeeBatchAccess(Employee requester, List<Long> targetEmployeeIds) {
        if (hasFullEmployeeScope(requester)) {
            return;
        }
        Set<Long> allowedIds = getAllowedEmployeeIds(requester);
        allowedIds.add(requester.getEmployeeId());
        boolean hasOutOfScopeId = targetEmployeeIds.stream().anyMatch(id -> !allowedIds.contains(id));
        if (hasOutOfScopeId) {
            throw new AdminAccessDeniedException();
        }
    }

    private void validateLeaderScope(Employee requester, Long leaderId) {
        if (leaderId == null) {
            throw new AdminAccessDeniedException();
        }
        if (hasFullEmployeeScope(requester)) {
            return;
        }
        if (!requester.getEmployeeId().equals(leaderId)) {
            throw new AdminAccessDeniedException();
        }
    }

    private void validateDepartmentScope(Employee requester, Long departmentId) {
        if (departmentId == null) {
            throw new AdminAccessDeniedException();
        }
        if (hasFullEmployeeScope(requester)) {
            return;
        }
        Long requesterDepartmentId = requester.getDepartmentId();
        if (requesterDepartmentId == null) {
            throw new AdminAccessDeniedException();
        }
        if (requester.getEmployeeRole() == EmployeeRole.TL) {
            if (!requesterDepartmentId.equals(departmentId)) {
                throw new AdminAccessDeniedException();
            }
            return;
        }
        if (requester.getEmployeeRole() == EmployeeRole.DL) {
            Set<Long> accessibleDepartments = getAccessibleDepartmentIds(requesterDepartmentId);
            if (!accessibleDepartments.contains(departmentId)) {
                throw new AdminAccessDeniedException();
            }
            return;
        }
        throw new AdminAccessDeniedException();
    }

    private boolean hasFullEmployeeScope(Employee requester) {
        EmployeeRole role = requester.getEmployeeRole();
        return role == EmployeeRole.ADMIN || role == EmployeeRole.HRM;
    }

    private Set<Long> getAllowedEmployeeIds(Employee requester) {
        EmployeeRole role = requester.getEmployeeRole();
        if (role == EmployeeRole.TL || role == EmployeeRole.DL) {
            return new HashSet<>(employeeMapper.findTeamMemberIdsByLeaderId(requester.getEmployeeId()));
        }
        if (role == EmployeeRole.WORKER) {
            return new HashSet<>(Set.of(requester.getEmployeeId()));
        }
        return new HashSet<>();
    }

    private Set<Long> getAccessibleDepartmentIds(Long rootDepartmentId) {
        Set<Long> departmentIds = new HashSet<>();
        ArrayDeque<Long> queue = new ArrayDeque<>();
        departmentIds.add(rootDepartmentId);
        queue.add(rootDepartmentId);

        while (!queue.isEmpty()) {
            Long currentDepartmentId = queue.removeFirst();
            for (Department child : departmentRepository.findByParentDepartmentIdAndIsDeletedFalse(currentDepartmentId)) {
                if (departmentIds.add(child.getDepartmentId())) {
                    queue.addLast(child.getDepartmentId());
                }
            }
        }
        return departmentIds;
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeRoleChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeRoleChangeResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.rolechangehistory.RoleChangeHistory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.RoleChangeHistoryRepository;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeRoleManageCommandService {

    private final EmployeeRepository employeeRepository;
    private final RoleChangeHistoryRepository roleChangeHistoryRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public EmployeeRoleChangeResponse changeEmployeeRole(EmployeeRoleChangeRequest request, String adminCode) {

        Employee admin = employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .orElseThrow(EmployeeNotFoundException::new);

        RoleChangeHistory history = RoleChangeHistory.builder()
                .roleChangeHistoryId(idGenerator.generate())
                .targetEmployeeId(target.getEmployeeId())
                .changedBy(admin.getEmployeeId())
                .previousRole(target.getEmployeeRole())
                .newRole(request.getNewRole())
                .reason(request.getReason())
                .effectiveDate(request.getEffectiveDate())
                .build();

        roleChangeHistoryRepository.save(history);

        // TODO: Employee.updateRole()은 effectiveDate에 배치 시스템에서 실행됨

        return EmployeeRoleChangeResponse.builder()
                .employeeCode(request.getEmployeeCode())
                .newRole(request.getNewRole())
                .reason(request.getReason())
                .effectiveDate(request.getEffectiveDate())
                .build();
    }
}

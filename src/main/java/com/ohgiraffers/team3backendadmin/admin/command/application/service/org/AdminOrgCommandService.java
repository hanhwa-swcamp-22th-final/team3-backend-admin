package com.ohgiraffers.team3backendadmin.admin.command.application.service.org;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamMemberRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrgCommandService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository   employeeRepository;
    private final IdGenerator          idGenerator;

    /** HR-076: 부서 생성 */
    @Transactional
    public Long createDepartment(OrgDepartmentRequest request) {
        Long newId = idGenerator.generate();
        Department dept = Department.builder()
                .departmentId(newId)
                .parentDepartmentId(request.getParentDepartmentId())
                .departmentName(request.getDepartmentName())
                .teamName(request.getTeamName())
                .depth(request.getDepth() != null ? request.getDepth() : "DEPARTMENT")
                .build();
        departmentRepository.save(dept);
        return newId;
    }

    /** HR-077: 부서 수정 */
    @Transactional
    public Long updateDepartment(Long departmentId, OrgDepartmentRequest request) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);
        dept.updateNames(request.getDepartmentName(), request.getTeamName());
        return departmentId;
    }

    /** HR-078: 부서 삭제 */
    @Transactional
    public void deleteDepartment(Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);
        dept.softDelete();
    }

    /** HR-079: 팀 생성 (부서의 하위 팀) */
    @Transactional
    public Long createTeam(Long departmentId, OrgTeamRequest request) {
        // 부모 부서 존재 여부 확인
        departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        Long newId = idGenerator.generate();
        Department team = Department.builder()
                .departmentId(newId)
                .parentDepartmentId(departmentId)
                .departmentName(request.getTeamName())
                .teamName(request.getTeamName())
                .depth("TEAM")
                .build();
        departmentRepository.save(team);

        // 팀장 배치
        if (request.getLeaderId() != null) {
            employeeRepository.findById(request.getLeaderId())
                    .ifPresent(emp -> emp.assignDepartment(newId));
        }

        return newId;
    }

    /** HR-080: 팀 수정 */
    @Transactional
    public Long updateTeam(Long teamId, OrgTeamRequest request) {
        Department team = departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);
        team.updateNames(request.getTeamName(), request.getTeamName());

        // 팀장 변경
        if (request.getLeaderId() != null) {
            employeeRepository.findById(request.getLeaderId())
                    .ifPresent(emp -> emp.assignDepartment(teamId));
        }

        return teamId;
    }

    /** HR-081: 팀 삭제 */
    @Transactional
    public void deleteTeam(Long teamId) {
        Department team = departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);

        // 팀원 부서 해제 (상위 부서로 이동)
        Long parentDeptId = team.getParentDepartmentId();
        List<Employee> members = employeeRepository.findAll().stream()
                .filter(e -> teamId.equals(e.getDepartmentId()))
                .toList();
        members.forEach(emp -> emp.assignDepartment(parentDeptId));

        team.softDelete();
    }

    /** HR-083: 팀원 추가 */
    @Transactional
    public void addTeamMembers(Long teamId, OrgTeamMemberRequest request) {
        departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);
        if (request.getEmployeeIds() == null) return;
        for (Long empId : request.getEmployeeIds()) {
            employeeRepository.findById(empId)
                    .ifPresent(emp -> emp.assignDepartment(teamId));
        }
    }

    /** HR-084: 팀원 제거 */
    @Transactional
    public void removeTeamMember(Long teamId, Long employeeId) {
        Department team = departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
        // 상위 부서로 이동
        emp.assignDepartment(team.getParentDepartmentId());
    }
}

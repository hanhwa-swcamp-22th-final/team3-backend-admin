package com.ohgiraffers.team3backendadmin.admin.command.application.service.org;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentLeaderRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamMemberRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrgCommandService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository   employeeRepository;
    private final IdGenerator          idGenerator;
    private final OrgEmployeeTransferService orgEmployeeTransferService;

    /** HR-076: 부서 생성 */
    @Transactional
    public Long createDepartment(OrgDepartmentRequest request) {
        Long newId = idGenerator.generate();
        Long parentDepartmentId = request.getParentDepartmentId();
        Department dept = Department.builder()
                .departmentId(newId)
                .parentDepartmentId(parentDepartmentId)
                .departmentName(request.getDepartmentName())
                .teamName(request.getTeamName())
                .depth(resolveDepth(parentDepartmentId))
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
                .depth(resolveDepth(departmentId))
                .build();
        departmentRepository.save(team);

        // 팀장 배치
        if (request.getLeaderId() != null) {
            orgEmployeeTransferService.transfer(request.getLeaderId(), newId);
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
            orgEmployeeTransferService.transfer(request.getLeaderId(), teamId);
        }

        return teamId;
    }

    /** HR-081: 팀 삭제 */
    @Transactional
    public void deleteTeam(Long teamId) {
        Department team = departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);

        team.softDelete();
    }

    /** HR-083: 팀원 추가 */
    @Transactional
    public void addTeamMembers(Long teamId, OrgTeamMemberRequest request) {
        departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);
        if (request.getEmployeeIds() == null) return;
        for (Long empId : request.getEmployeeIds()) {
            orgEmployeeTransferService.transfer(empId, teamId);
        }
    }

    /** HR-084: 팀원 제거 */
    @Transactional
    public void removeTeamMember(Long teamId, Long employeeId) {
        Department team = departmentRepository.findById(teamId)
                .orElseThrow(DepartmentNotFoundException::new);
        // 상위 부서가 있으면 상위 부서로 이동, 없으면 현재 팀 ID 그대로 유지
        Long parentId = team.getParentDepartmentId();
        if (parentId != null) {
            orgEmployeeTransferService.transfer(employeeId, parentId);
        }
    }

    /** HR-085: 부서장 지정 */
    @Transactional
    public Long assignDepartmentLeader(Long departmentId, OrgDepartmentLeaderRequest request) {
        departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentNotFoundException::new);

        return orgEmployeeTransferService.transfer(request.getEmployeeId(), departmentId).getEmployeeId();
    }

    private String resolveDepth(Long parentDepartmentId) {
        if (parentDepartmentId == null) {
            return "L0";
        }

        Department parent = departmentRepository.findById(parentDepartmentId)
                .orElseThrow(DepartmentNotFoundException::new);
        return "L" + (parseDepthLevel(parent.getDepth()) + 1);
    }

    private int parseDepthLevel(String depth) {
        if (depth == null || depth.isBlank()) {
            return 0;
        }
        if ("DEPARTMENT".equalsIgnoreCase(depth)) {
            return 0;
        }
        if ("TEAM".equalsIgnoreCase(depth)) {
            return 1;
        }
        if (depth.length() > 1 && (depth.charAt(0) == 'L' || depth.charAt(0) == 'l')) {
            try {
                return Integer.parseInt(depth.substring(1));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }
}

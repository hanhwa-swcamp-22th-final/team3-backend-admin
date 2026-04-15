package com.ohgiraffers.team3backendadmin.admin.command.application.service.org;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentLeaderRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamMemberRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminOrgCommandServiceTest {

    @InjectMocks
    private AdminOrgCommandService adminOrgCommandService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private OrgEmployeeTransferService orgEmployeeTransferService;

    @Test
    @DisplayName("최상위 부서는 depth L0로 생성한다")
    void createDepartmentUsesL0Depth() {
        OrgDepartmentRequest request = new OrgDepartmentRequest();
        request.setDepartmentName("생산본부");
        request.setDepth("DEPARTMENT");
        given(idGenerator.generate()).willReturn(100L);
        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        Long result = adminOrgCommandService.createDepartment(request);

        assertEquals(100L, result);
        verify(departmentRepository).save(captor.capture());
        assertEquals("L0", captor.getValue().getDepth());
    }

    @Test
    @DisplayName("하위 부서는 부모 depth 기준으로 다음 depth를 생성한다")
    void createTeamUsesNextLevelDepth() {
        OrgTeamRequest request = new OrgTeamRequest();
        request.setTeamName("가공1팀");
        given(departmentRepository.findById(100L)).willReturn(Optional.of(department(100L, null, "L0")));
        given(idGenerator.generate()).willReturn(200L);
        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        Long result = adminOrgCommandService.createTeam(100L, request);

        assertEquals(200L, result);
        verify(departmentRepository).save(captor.capture());
        assertEquals("L1", captor.getValue().getDepth());
    }

    @Test
    @DisplayName("소속 직원이나 하위 팀이 있어도 부서를 소프트 삭제한다")
    void deleteDepartmentSoftDeletesWithoutMovingMembers() {
        Department department = department(100L, null, "DEPARTMENT");
        given(departmentRepository.findById(100L)).willReturn(Optional.of(department));

        adminOrgCommandService.deleteDepartment(100L);

        assertTrue(department.getIsDeleted());
        assertEquals("조직", department.getDepartmentName());
        assertEquals("DEPARTMENT", department.getDepth());
    }

    @Test
    @DisplayName("소속 직원이 있어도 팀을 소프트 삭제한다")
    void deleteTeamSoftDeletesWithoutMovingMembers() {
        Department team = department(200L, 100L, "TEAM");
        given(departmentRepository.findById(200L)).willReturn(Optional.of(team));

        adminOrgCommandService.deleteTeam(200L);

        assertTrue(team.getIsDeleted());
        assertEquals(100L, team.getParentDepartmentId());
        assertEquals("TEAM", team.getDepth());
    }

    @Test
    @DisplayName("팀원 추가는 발령 정책 서비스를 사용한다")
    void addTeamMembersUsesTransferPolicy() {
        OrgTeamMemberRequest request = new OrgTeamMemberRequest();
        request.setEmployeeIds(List.of(10L, 20L));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, 100L, "TEAM")));

        adminOrgCommandService.addTeamMembers(200L, request);

        verify(orgEmployeeTransferService).transfer(10L, 200L);
        verify(orgEmployeeTransferService).transfer(20L, 200L);
    }

    @Test
    @DisplayName("부서장 지정은 발령 정책 서비스를 사용한다")
    void assignDepartmentLeaderUsesTransferPolicy() {
        OrgDepartmentLeaderRequest request = new OrgDepartmentLeaderRequest();
        request.setEmployeeId(10L);
        Employee leader = employee(10L);
        given(departmentRepository.findById(100L)).willReturn(Optional.of(department(100L, null, "DEPARTMENT")));
        given(orgEmployeeTransferService.transfer(10L, 100L)).willReturn(leader);

        Long result = adminOrgCommandService.assignDepartmentLeader(100L, request);

        assertEquals(10L, result);
        verify(orgEmployeeTransferService).transfer(10L, 100L);
    }

    private Department department(Long departmentId, Long parentDepartmentId, String depth) {
        return Department.builder()
                .departmentId(departmentId)
                .parentDepartmentId(parentDepartmentId)
                .departmentName("조직")
                .teamName("팀")
                .depth(depth)
                .build();
    }

    private Employee employee(Long employeeId) {
        return Employee.builder()
                .employeeId(employeeId)
                .build();
    }
}

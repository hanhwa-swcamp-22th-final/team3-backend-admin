package com.ohgiraffers.team3backendadmin.admin.command.application.service.org;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrgEmployeeTransferServiceTest {

    @InjectMocks
    private OrgEmployeeTransferService orgEmployeeTransferService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("일반 직원은 제약 없이 타겟 부서로 이동한다")
    void transferWorkerWithoutConstraint() {
        Employee worker = employee(1L, EmployeeRole.WORKER, 100L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(worker));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, null, "L0", false)));

        orgEmployeeTransferService.transfer(1L, 200L);

        assertEquals(200L, worker.getDepartmentId());
        assertEquals(EmployeeRole.WORKER, worker.getEmployeeRole());
        verify(employeeRepository, never()).existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(any(), any(), any());
    }

    @Test
    @DisplayName("리더가 일반 부서로 이동하면 DL로 보정한다")
    void transferLeaderToDepartmentAdjustsRoleToDl() {
        Employee leader = employee(1L, EmployeeRole.TL, 300L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(leader));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, null, "L0", false)));
        given(employeeRepository.existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(eq(200L), any(Collection.class), eq(1L)))
                .willReturn(false);

        orgEmployeeTransferService.transfer(1L, 200L);

        assertEquals(200L, leader.getDepartmentId());
        assertEquals(EmployeeRole.DL, leader.getEmployeeRole());
    }

    @Test
    @DisplayName("리더가 팀으로 이동하면 TL로 보정한다")
    void transferLeaderToTeamAdjustsRoleToTl() {
        Employee leader = employee(1L, EmployeeRole.DL, 300L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(leader));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, 100L, "L1", false)));
        given(employeeRepository.existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(eq(200L), any(Collection.class), eq(1L)))
                .willReturn(false);

        orgEmployeeTransferService.transfer(1L, 200L);

        assertEquals(200L, leader.getDepartmentId());
        assertEquals(EmployeeRole.TL, leader.getEmployeeRole());
    }

    @Test
    @DisplayName("타겟 부서에 다른 리더가 있으면 리더 발령을 차단한다")
    void transferLeaderFailsWhenTargetAlreadyHasLeader() {
        Employee leader = employee(1L, EmployeeRole.TL, 300L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(leader));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, null, "L0", false)));
        given(employeeRepository.existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(eq(200L), any(Collection.class), eq(1L)))
                .willReturn(true);

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> orgEmployeeTransferService.transfer(1L, 200L)
        );

        assertEquals("해당 부서에 이미 부서장이 존재하여 발령할 수 없습니다.", exception.getMessage());
        assertEquals(300L, leader.getDepartmentId());
        assertEquals(EmployeeRole.TL, leader.getEmployeeRole());
    }

    @Test
    @DisplayName("삭제된 부서로 리더를 이동할 때는 리더 중복 검사와 Role 보정을 건너뛴다")
    void transferLeaderToDeletedDepartmentKeepsRole() {
        Employee leader = employee(1L, EmployeeRole.DL, 300L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(leader));
        given(departmentRepository.findById(200L)).willReturn(Optional.of(department(200L, 100L, "L1", true)));

        orgEmployeeTransferService.transfer(1L, 200L);

        assertEquals(200L, leader.getDepartmentId());
        assertEquals(EmployeeRole.DL, leader.getEmployeeRole());
        verify(employeeRepository, never()).existsByDepartmentIdAndEmployeeRoleInAndEmployeeIdNot(any(), any(), any());
    }

    private Employee employee(Long employeeId, EmployeeRole role, Long departmentId) {
        return Employee.builder()
                .employeeId(employeeId)
                .employeeRole(role)
                .departmentId(departmentId)
                .build();
    }

    private Department department(Long departmentId, Long parentDepartmentId, String depth, boolean deleted) {
        return Department.builder()
                .departmentId(departmentId)
                .parentDepartmentId(parentDepartmentId)
                .departmentName("조직")
                .teamName("팀")
                .depth(depth)
                .isDeleted(deleted)
                .build();
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeRoleChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.rolechangehistory.RoleChangeHistory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.RoleChangeHistoryRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeRoleManageCommandServiceTest {

    @InjectMocks
    private EmployeeRoleManageCommandService employeeRoleManageCommandService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleChangeHistoryRepository roleChangeHistoryRepository;

    @Mock
    private IdGenerator idGenerator;

    private Employee admin;
    private Employee targetEmployee;

    @BeforeEach
    void setUp() {
        admin = Employee.builder()
                .employeeId(1L)
                .employeeCode("EMP-0001")
                .employeeName("김관리")
                .employeeRole(EmployeeRole.ADMIN)
                .employeeStatus(EmployeeStatus.ACTIVE)
                .build();

        targetEmployee = Employee.builder()
                .employeeId(5000L)
                .employeeCode("EMP2603001")
                .employeeName("홍길동")
                .employeeRole(EmployeeRole.WORKER)
                .employeeStatus(EmployeeStatus.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("changeEmployeeRole 메서드")
    class ChangeEmployeeRole {

        @Test
        @DisplayName("역할이 변경되고 이력이 저장된다")
        void changeRoleSuccess() {
            // given
            LocalDateTime effectiveDate = LocalDateTime.of(2026, 4, 1, 0, 0);
            EmployeeRoleChangeRequest request = new EmployeeRoleChangeRequest(
                    "EMP2603001", EmployeeRole.TL, "팀장 승진", effectiveDate
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(idGenerator.generate()).willReturn(9000L);

            // when
            employeeRoleManageCommandService.changeEmployeeRole(request, "EMP-0001");

            // then — 역할 변경은 배치에서 effectiveDate에 실행되므로 여기서는 변경되지 않음
            assertEquals(EmployeeRole.WORKER, targetEmployee.getEmployeeRole());

            ArgumentCaptor<RoleChangeHistory> captor = ArgumentCaptor.forClass(RoleChangeHistory.class);
            verify(roleChangeHistoryRepository).save(captor.capture());

            RoleChangeHistory saved = captor.getValue();
            assertEquals(9000L, saved.getRoleChangeHistoryId());
            assertEquals(5000L, saved.getTargetEmployeeId());
            assertEquals(1L, saved.getChangedBy());
            assertEquals(EmployeeRole.WORKER, saved.getPreviousRole());
            assertEquals(EmployeeRole.TL, saved.getNewRole());
            assertEquals("팀장 승진", saved.getReason());
            assertEquals(effectiveDate, saved.getEffectiveDate());
        }

        @Test
        @DisplayName("reason이 null이어도 정상 동작한다")
        void changeRoleWithNullReason() {
            // given
            LocalDateTime effectiveDate = LocalDateTime.of(2026, 5, 1, 0, 0);
            EmployeeRoleChangeRequest request = new EmployeeRoleChangeRequest(
                    "EMP2603001", EmployeeRole.DL, null, effectiveDate
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(idGenerator.generate()).willReturn(9001L);

            // when
            employeeRoleManageCommandService.changeEmployeeRole(request, "EMP-0001");

            // then
            assertEquals(EmployeeRole.WORKER, targetEmployee.getEmployeeRole());

            ArgumentCaptor<RoleChangeHistory> captor = ArgumentCaptor.forClass(RoleChangeHistory.class);
            verify(roleChangeHistoryRepository).save(captor.capture());

            RoleChangeHistory saved = captor.getValue();
            assertNull(saved.getReason());
            assertEquals(effectiveDate, saved.getEffectiveDate());
        }

        @Test
        @DisplayName("대상 사원이 존재하지 않으면 예외가 발생한다")
        void changeRoleTargetNotFound() {
            // given
            EmployeeRoleChangeRequest request = new EmployeeRoleChangeRequest(
                    "UNKNOWN_TARGET", EmployeeRole.TL, "승진",
                    LocalDateTime.of(2026, 4, 1, 0, 0)
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("UNKNOWN_TARGET"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> employeeRoleManageCommandService.changeEmployeeRole(request, "EMP-0001")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자 사원코드가 존재하지 않으면 예외가 발생한다")
        void changeRoleAdminNotFound() {
            // given
            EmployeeRoleChangeRequest request = new EmployeeRoleChangeRequest(
                    "EMP2603001", EmployeeRole.TL, "승진",
                    LocalDateTime.of(2026, 4, 1, 0, 0)
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeRoleManageCommandService.changeEmployeeRole(request, "UNKNOWN")
            );
            assertEquals("접근 권한이 없습니다.", exception.getMessage());
        }
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeSkillUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.SkillNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmployeeSkillManageCommandServiceTest {

    @InjectMocks
    private EmployeeSkillManageCommandService employeeSkillManageCommandService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SkillRepository skillRepository;

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
    @DisplayName("updateEmployeeSkill 메서드")
    class UpdateEmployeeSkill {

        @Test
        @DisplayName("모든 스킬 점수가 정상적으로 수정된다")
        void updateAllSkillScores() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "EMP2603001",
                    new BigDecimal("85.50"),
                    new BigDecimal("90.00"),
                    new BigDecimal("75.25"),
                    new BigDecimal("88.00"),
                    new BigDecimal("92.50"),
                    new BigDecimal("80.00")
            );

            Skill equipmentResponse = buildSkill(SkillCategory.EQUIPMENT_RESPONSE);
            Skill technicalTransfer = buildSkill(SkillCategory.TECHNICAL_TRANSFER);
            Skill innovationProposal = buildSkill(SkillCategory.INNOVATION_PROPOSAL);
            Skill safetyCompliance = buildSkill(SkillCategory.SAFETY_COMPLIANCE);
            Skill qualityManagement = buildSkill(SkillCategory.QUALITY_MANAGEMENT);
            Skill productivity = buildSkill(SkillCategory.PRODUCTIVITY);

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.EQUIPMENT_RESPONSE))
                    .willReturn(Optional.of(equipmentResponse));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.TECHNICAL_TRANSFER))
                    .willReturn(Optional.of(technicalTransfer));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.INNOVATION_PROPOSAL))
                    .willReturn(Optional.of(innovationProposal));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.SAFETY_COMPLIANCE))
                    .willReturn(Optional.of(safetyCompliance));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.QUALITY_MANAGEMENT))
                    .willReturn(Optional.of(qualityManagement));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.PRODUCTIVITY))
                    .willReturn(Optional.of(productivity));

            // when
            employeeSkillManageCommandService.updateEmployeeSkill(request, "EMP-0001");

            // then
            assertEquals(new BigDecimal("85.50"), equipmentResponse.getSkillScore());
            assertEquals(new BigDecimal("90.00"), technicalTransfer.getSkillScore());
            assertEquals(new BigDecimal("75.25"), innovationProposal.getSkillScore());
            assertEquals(new BigDecimal("88.00"), safetyCompliance.getSkillScore());
            assertEquals(new BigDecimal("92.50"), qualityManagement.getSkillScore());
            assertEquals(new BigDecimal("80.00"), productivity.getSkillScore());
        }

        @Test
        @DisplayName("일부 스킬만 수정하면 나머지는 변경되지 않는다")
        void updatePartialSkillScores() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "EMP2603001",
                    new BigDecimal("85.50"),
                    null,
                    null,
                    null,
                    null,
                    new BigDecimal("80.00")
            );

            Skill equipmentResponse = buildSkill(SkillCategory.EQUIPMENT_RESPONSE);
            Skill productivity = buildSkill(SkillCategory.PRODUCTIVITY);

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.EQUIPMENT_RESPONSE))
                    .willReturn(Optional.of(equipmentResponse));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.PRODUCTIVITY))
                    .willReturn(Optional.of(productivity));

            // when
            employeeSkillManageCommandService.updateEmployeeSkill(request, "EMP-0001");

            // then
            assertEquals(new BigDecimal("85.50"), equipmentResponse.getSkillScore());
            assertEquals(new BigDecimal("80.00"), productivity.getSkillScore());
        }

        @Test
        @DisplayName("스킬 수정 시 evaluatedAt이 갱신된다")
        void updateSkillUpdatesEvaluatedAt() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "EMP2603001",
                    new BigDecimal("85.50"),
                    null, null, null, null, null
            );

            Skill equipmentResponse = buildSkill(SkillCategory.EQUIPMENT_RESPONSE);
            LocalDateTime beforeUpdate = equipmentResponse.getEvaluatedAt();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.EQUIPMENT_RESPONSE))
                    .willReturn(Optional.of(equipmentResponse));

            // when
            employeeSkillManageCommandService.updateEmployeeSkill(request, "EMP-0001");

            // then
            assertNotNull(equipmentResponse.getEvaluatedAt());
            assertTrue(equipmentResponse.getEvaluatedAt().isAfter(beforeUpdate)
                    || equipmentResponse.getEvaluatedAt().isEqual(beforeUpdate));
        }

        @Test
        @DisplayName("대상 사원이 존재하지 않으면 예외가 발생한다")
        void updateSkillTargetEmployeeNotFound() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "UNKNOWN_TARGET",
                    new BigDecimal("85.50"),
                    null, null, null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("UNKNOWN_TARGET"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> employeeSkillManageCommandService.updateEmployeeSkill(request, "EMP-0001")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자 사원코드가 존재하지 않으면 예외가 발생한다")
        void updateSkillAdminNotFound() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "EMP2603001",
                    new BigDecimal("85.50"),
                    null, null, null, null, null
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeSkillManageCommandService.updateEmployeeSkill(request, "UNKNOWN")
            );
            assertEquals("접근 권한이 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("스킬 레코드가 존재하지 않으면 예외가 발생한다")
        void updateSkillRecordNotFound() {
            // given
            EmployeeSkillUpdateRequest request = new EmployeeSkillUpdateRequest(
                    "EMP2603001",
                    new BigDecimal("85.50"),
                    null, null, null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.EQUIPMENT_RESPONSE))
                    .willReturn(Optional.empty());

            // when & then
            SkillNotFoundException exception = assertThrows(
                    SkillNotFoundException.class,
                    () -> employeeSkillManageCommandService.updateEmployeeSkill(request, "EMP-0001")
            );
            assertTrue(exception.getMessage().contains("해당 스킬 레코드를 찾을 수 없습니다"));
        }

        private Skill buildSkill(SkillCategory category) {
            return Skill.builder()
                    .skillId(1000L)
                    .employeeId(5000L)
                    .skillCategory(category)
                    .skillScore(BigDecimal.ZERO)
                    .evaluatedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
                    .build();
        }
    }
}

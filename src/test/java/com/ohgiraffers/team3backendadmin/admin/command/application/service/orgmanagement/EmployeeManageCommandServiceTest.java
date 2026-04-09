package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeDepartmentMatchRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeDepartmentMatchResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.consent.Consent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.ConsentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
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
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeManageCommandServiceTest {

    @InjectMocks
    private EmployeeManageCommandService employeeManageCommandService;

    @Mock
    private OrganizationManageDomainService organizationManageDomainService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ConsentRepository consentRepository;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AesEncryptor aesEncryptor;

    private Employee admin;

    @BeforeEach
    void setUp() {
        admin = Employee.builder()
                .employeeId(1L)
                .employeeCode("EMP-0001")
                .employeeName("김관리")
                .employeeRole(EmployeeRole.ADMIN)
                .employeeStatus(EmployeeStatus.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("insertEmployee 메서드")
    class InsertEmployee {

        private EmployeeCreateRequest createRequest() {
            return new EmployeeCreateRequest(
                    "홍길동",
                    "hong@company.com",
                    "010-1234-5678",
                    "서울시 강남구",
                    "010-9876-5432",
                    "password123",
                    EmployeeRole.WORKER,
                    EmployeeStatus.ACTIVE,
                    EmployeeTier.B,
                    LocalDate.of(2026, 4, 9),
                    new BigDecimal("85.00"),
                    new BigDecimal("70.00"),
                    new BigDecimal("60.00"),
                    new BigDecimal("90.00"),
                    new BigDecimal("75.00"),
                    new BigDecimal("80.00")
            );
        }

        @Test
        @DisplayName("사원이 정상적으로 등록된다")
        void insertEmployeeSuccess() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(aesEncryptor.encrypt(anyString())).willAnswer(invocation -> "AES_" + invocation.getArgument(0));
            given(employeeRepository.existsByEmployeeEmail("AES_hong@company.com")).willReturn(false);
            given(employeeRepository.existsByEmployeePhone("AES_010-1234-5678")).willReturn(false);
            given(idGenerator.generate()).willReturn(5000L);
            given(organizationManageDomainService.generateEmployeeCode()).willReturn("EMP2603001");
            given(passwordEncoder.encode(anyString())).willAnswer(invocation -> "$2a$10$" + invocation.getArgument(0));

            // when
            EmployeeCreateResponse response = employeeManageCommandService.insertEmployee(request, "EMP-0001");

            // then
            assertEquals("EMP2603001", response.getEmployeeCode());

            ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(captor.capture());

            Employee saved = captor.getValue();
            assertEquals(5000L, saved.getEmployeeId());
            assertNull(saved.getDepartmentId());
            assertEquals("EMP2603001", saved.getEmployeeCode());
            assertEquals("홍길동", saved.getEmployeeName());
            assertEquals("AES_hong@company.com", saved.getEmployeeEmail());
            assertEquals("AES_010-1234-5678", saved.getEmployeePhone());
            assertEquals("AES_서울시 강남구", saved.getEmployeeAddress());
            assertEquals("AES_010-9876-5432", saved.getEmployeeEmergencyContact());
            assertEquals("$2a$10$password123", saved.getEmployeePassword());
            assertEquals(EmployeeRole.WORKER, saved.getEmployeeRole());
            assertEquals(EmployeeStatus.ACTIVE, saved.getEmployeeStatus());
            assertEquals(EmployeeTier.B, saved.getEmployeeTier());
            assertEquals(LocalDate.of(2026, 4, 9), saved.getHireDate());
            assertFalse(saved.getMfaEnabled());
            assertEquals(0, saved.getLoginFailCount());
            assertFalse(saved.getIsLocked());

            // 6개의 스킬 레코드가 사용자 입력값으로 생성되는지 확인
            ArgumentCaptor<Skill> skillCaptor = ArgumentCaptor.forClass(Skill.class);
            verify(skillRepository, times(6)).save(skillCaptor.capture());

            java.util.List<Skill> savedSkills = skillCaptor.getAllValues();
            java.util.Map<com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory, BigDecimal> scoreMap = new java.util.HashMap<>();
            savedSkills.forEach(s -> scoreMap.put(s.getSkillCategory(), s.getSkillScore()));

            assertEquals(new BigDecimal("85.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.EQUIPMENT_RESPONSE));
            assertEquals(new BigDecimal("70.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.TECHNICAL_TRANSFER));
            assertEquals(new BigDecimal("60.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.INNOVATION_PROPOSAL));
            assertEquals(new BigDecimal("90.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.SAFETY_COMPLIANCE));
            assertEquals(new BigDecimal("75.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.QUALITY_MANAGEMENT));
            assertEquals(new BigDecimal("80.00"), scoreMap.get(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory.PRODUCTIVITY));

            // 기본 약관 동의 레코드가 생성되는지 확인
            verify(consentRepository).save(any(Consent.class));
        }

        @Test
        @DisplayName("존재하지 않는 사원코드이면 예외가 발생한다")
        void insertEmployeeAdminNotFound() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeManageCommandService.insertEmployee(request, "UNKNOWN")
            );
            assertEquals("접근 권한이 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("이미 사용중인 이메일이면 예외가 발생한다")
        void insertEmployeeDuplicateEmail() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(aesEncryptor.encrypt("hong@company.com"))
                    .willReturn("AES_hong@company.com");
            given(employeeRepository.existsByEmployeeEmail("AES_hong@company.com"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> employeeManageCommandService.insertEmployee(request, "EMP-0001")
            );
            assertEquals("이미 사용중인 이메일 입니다", exception.getMessage());
        }

        @Test
        @DisplayName("이미 사용중인 전화번호이면 예외가 발생한다")
        void insertEmployeeDuplicatePhone() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(aesEncryptor.encrypt("hong@company.com"))
                    .willReturn("AES_hong@company.com");
            given(employeeRepository.existsByEmployeeEmail("AES_hong@company.com"))
                    .willReturn(false);
            given(aesEncryptor.encrypt("010-1234-5678"))
                    .willReturn("AES_010-1234-5678");
            given(employeeRepository.existsByEmployeePhone("AES_010-1234-5678"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> employeeManageCommandService.insertEmployee(request, "EMP-0001")
            );
            assertEquals("이미 사용중인 전화번호 입니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteEmployee 메서드")
    class DeleteEmployee {

        private Employee targetEmployee;

        @BeforeEach
        void setUp() {
            targetEmployee = Employee.builder()
                    .employeeId(5000L)
                    .employeeCode("EMP2603001")
                    .employeeStatus(EmployeeStatus.ACTIVE)
                    .build();
        }

        @Test
        @DisplayName("사원의 상태가 ON_LEAVE로 변경된다")
        void deleteEmployeeSuccess() {
            // given
            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));

            // when
            employeeManageCommandService.deleteEmployee("EMP2603001", "EMP-0001");

            // then
            assertEquals(EmployeeStatus.ON_LEAVE, targetEmployee.getEmployeeStatus());
        }

        @Test
        @DisplayName("대상 사원이 존재하지 않으면 예외가 발생한다")
        void deleteEmployeeTargetNotFound() {
            // given
            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("UNKNOWN_TARGET"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> employeeManageCommandService.deleteEmployee("UNKNOWN_TARGET", "EMP-0001")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자 사원코드가 존재하지 않으면 예외가 발생한다")
        void deleteEmployeeAdminNotFound() {
            // given
            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeManageCommandService.deleteEmployee("EMP2603001", "UNKNOWN")
            );
            assertEquals("접근 권한이 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("matchDepartment 메서드")
    class MatchDepartment {

        private Employee targetEmployee;

        @BeforeEach
        void setUp() {
            targetEmployee = Employee.builder()
                    .employeeId(5000L)
                    .employeeCode("EMP2603001")
                    .employeeName("홍길동")
                    .departmentId(null)
                    .employeeStatus(EmployeeStatus.ACTIVE)
                    .build();
        }

        @Test
        @DisplayName("사원에게 부서가 정상적으로 배치된다")
        void matchDepartmentSuccess() {
            // given
            EmployeeDepartmentMatchRequest request = new EmployeeDepartmentMatchRequest("EMP2603001", 100L);

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(departmentRepository.findById(100L))
                    .willReturn(Optional.of(Department.builder().departmentId(100L).build()));

            // when
            EmployeeDepartmentMatchResponse response = employeeManageCommandService.matchDepartment(request, "EMP-0001");

            // then
            assertEquals(100L, targetEmployee.getDepartmentId());
            assertEquals("홍길동", response.getEmployeeName());
            assertEquals("EMP2603001", response.getEmployeeCode());
            assertEquals(100L, response.getDepartmentId());
        }

        @Test
        @DisplayName("대상 사원이 존재하지 않으면 예외가 발생한다")
        void matchDepartmentEmployeeNotFound() {
            // given
            EmployeeDepartmentMatchRequest request = new EmployeeDepartmentMatchRequest("UNKNOWN", 100L);

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> employeeManageCommandService.matchDepartment(request, "EMP-0001")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("부서가 존재하지 않으면 예외가 발생한다")
        void matchDepartmentDepartmentNotFound() {
            // given
            EmployeeDepartmentMatchRequest request = new EmployeeDepartmentMatchRequest("EMP2603001", 999L);

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(departmentRepository.findById(999L))
                    .willReturn(Optional.empty());

            // when & then
            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> employeeManageCommandService.matchDepartment(request, "EMP-0001")
            );
            assertEquals("해당 부서를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("HRM 사원이 존재하지 않으면 예외가 발생한다")
        void matchDepartmentHrmNotFound() {
            // given
            EmployeeDepartmentMatchRequest request = new EmployeeDepartmentMatchRequest("EMP2603001", 100L);

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeManageCommandService.matchDepartment(request, "UNKNOWN")
            );
            assertEquals("접근 권한이 없습니다.", exception.getMessage());
        }
    }
}

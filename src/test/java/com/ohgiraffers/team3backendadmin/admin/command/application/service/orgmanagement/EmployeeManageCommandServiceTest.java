package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
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
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                    100L,
                    "홍길동",
                    "hong@company.com",
                    "010-1234-5678",
                    "서울시 강남구",
                    "010-9876-5432",
                    "password123",
                    EmployeeRole.WORKER,
                    EmployeeStatus.ACTIVE,
                    EmployeeTier.B
            );
        }

        @Test
        @DisplayName("사원이 정상적으로 등록된다")
        void insertEmployeeSuccess() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(100L))
                    .willReturn(Optional.of(Department.builder().departmentId(100L).build()));
            given(idGenerator.generate()).willReturn(5000L);
            given(organizationManageDomainService.generateEmployeeCode()).willReturn("EMP2603001");
            given(aesEncryptor.encrypt(anyString())).willAnswer(invocation -> "AES_" + invocation.getArgument(0));
            given(passwordEncoder.encode(anyString())).willAnswer(invocation -> "$2a$10$" + invocation.getArgument(0));

            // when
            employeeManageCommandService.insertEmployee(request, "EMP-0001");

            // then
            ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(captor.capture());

            Employee saved = captor.getValue();
            assertEquals(5000L, saved.getEmployeeId());
            assertEquals(100L, saved.getDepartmentId());
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
            assertFalse(saved.getMfaEnabled());
            assertEquals(0, saved.getLoginFailCount());
            assertFalse(saved.getIsLocked());

            // 6개의 기본 스킬 레코드가 생성되는지 확인
            verify(skillRepository, times(6)).save(any(Skill.class));
        }

        @Test
        @DisplayName("존재하지 않는 부서이면 예외가 발생한다")
        void insertEmployeeDepartmentNotFound() {
            // given
            EmployeeCreateRequest request = createRequest();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(100L))
                    .willReturn(Optional.empty());

            // when & then
            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> employeeManageCommandService.insertEmployee(request, "EMP-0001")
            );
            assertEquals("해당 부서를 찾을 수 없습니다", exception.getMessage());
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
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("updateEmployee 메서드")
    class UpdateEmployee {

        private Employee targetEmployee;

        @BeforeEach
        void setUp() {
            targetEmployee = Employee.builder()
                    .employeeId(5000L)
                    .departmentId(100L)
                    .employeeCode("EMP2603001")
                    .employeeName("홍길동")
                    .employeeEmail("AES_hong@company.com")
                    .employeePhone("AES_010-1234-5678")
                    .employeeAddress("AES_서울시 강남구")
                    .employeeEmergencyContact("AES_010-9876-5432")
                    .employeeRole(EmployeeRole.WORKER)
                    .employeeStatus(EmployeeStatus.ACTIVE)
                    .employeeTier(EmployeeTier.B)
                    .build();
        }

        @Test
        @DisplayName("모든 필드가 정상적으로 수정된다")
        void updateEmployeeAllFields() {
            // given
            EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                    "EMP2603001", "김철수", "kim@company.com",
                    "010-9999-8888", "부산시 해운대구", "010-1111-2222"
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(aesEncryptor.encrypt(anyString()))
                    .willAnswer(invocation -> "AES_" + invocation.getArgument(0));

            // when
            employeeManageCommandService.updateEmployee(request, "EMP-0001");

            // then
            assertEquals("김철수", targetEmployee.getEmployeeName());
            assertEquals("AES_kim@company.com", targetEmployee.getEmployeeEmail());
            assertEquals("AES_010-9999-8888", targetEmployee.getEmployeePhone());
            assertEquals("AES_부산시 해운대구", targetEmployee.getEmployeeAddress());
            assertEquals("AES_010-1111-2222", targetEmployee.getEmployeeEmergencyContact());
        }

        @Test
        @DisplayName("이름과 이메일만 수정하면 나머지 필드는 변경되지 않는다")
        void updateEmployeePartialFields() {
            // given
            EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                    "EMP2603001", "김철수", "kim@company.com",
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(targetEmployee));
            given(aesEncryptor.encrypt(anyString()))
                    .willAnswer(invocation -> "AES_" + invocation.getArgument(0));

            // when
            employeeManageCommandService.updateEmployee(request, "EMP-0001");

            // then
            assertEquals("김철수", targetEmployee.getEmployeeName());
            assertEquals("AES_kim@company.com", targetEmployee.getEmployeeEmail());
            assertEquals("AES_010-1234-5678", targetEmployee.getEmployeePhone());
            assertEquals("AES_서울시 강남구", targetEmployee.getEmployeeAddress());
            assertEquals("AES_010-9876-5432", targetEmployee.getEmployeeEmergencyContact());
        }

        @Test
        @DisplayName("대상 사원이 존재하지 않으면 예외가 발생한다")
        void updateEmployeeTargetNotFound() {
            // given
            EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                    "UNKNOWN_TARGET", "김철수", null,
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(employeeRepository.findByEmployeeCode("UNKNOWN_TARGET"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> employeeManageCommandService.updateEmployee(request, "EMP-0001")
            );
            assertEquals("해당 사원을 찾을 수 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("관리자 사원코드가 존재하지 않으면 예외가 발생한다")
        void updateEmployeeAdminNotFound() {
            // given
            EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                    "EMP2603001", "김철수", null,
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            AdminAccessDeniedException exception = assertThrows(
                    AdminAccessDeniedException.class,
                    () -> employeeManageCommandService.updateEmployee(request, "UNKNOWN")
            );
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
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
            assertEquals("해당 사원을 찾을 수 없습니다", exception.getMessage());
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
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }
}

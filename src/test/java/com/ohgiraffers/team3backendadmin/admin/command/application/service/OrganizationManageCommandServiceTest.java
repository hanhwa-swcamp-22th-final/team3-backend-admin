package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganizationManageCommandServiceTest {

    @InjectMocks
    private OrganizationManageCommandService organizationManageCommandService;

    @Mock
    private OrganizationManageDomainService organizationManageDomainService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

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
    @DisplayName("insertDepartment 메서드")
    class InsertDepartment {

        @Test
        @DisplayName("도메인 서비스에서 검증된 부서가 저장된다")
        void insertDepartmentSavesVerifiedDepartment() {
            // given
            DepartmentCreateRequest request = new DepartmentCreateRequest(
                    null, "경영지원본부", "시스템관리팀", "null"
            );

            Department verifiedDepartment = Department.builder()
                    .departmentId(1000L)
                    .parentDepartmentId(null)
                    .departmentName("경영지원본부")
                    .teamName(null)
                    .depth("L0")
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(idGenerator.generate()).willReturn(1000L);
            given(organizationManageDomainService.buildVerifiedDepartment(any(Department.class)))
                    .willReturn(verifiedDepartment);

            // when
            organizationManageCommandService.insertDepartment(request, "EMP-0001");

            // then
            verify(organizationManageDomainService).buildVerifiedDepartment(any(Department.class));
            verify(departmentRepository).save(verifiedDepartment);
        }

        @Test
        @DisplayName("도메인 서비스에 전달되는 부서에 요청 정보가 포함된다")
        void insertDepartmentPassesRequestInfo() {
            // given
            DepartmentCreateRequest request = new DepartmentCreateRequest(
                    50L, "개발본부", "백엔드팀", "L1"
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(idGenerator.generate()).willReturn(2000L);
            given(organizationManageDomainService.buildVerifiedDepartment(any(Department.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when
            organizationManageCommandService.insertDepartment(request, "EMP-0001");

            // then
            ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
            verify(organizationManageDomainService).buildVerifiedDepartment(captor.capture());

            Department passed = captor.getValue();
            assertEquals(2000L, passed.getDepartmentId());
            assertEquals(50L, passed.getParentDepartmentId());
            assertEquals("개발본부", passed.getDepartmentName());
            assertEquals("백엔드팀", passed.getTeamName());
        }

        @Test
        @DisplayName("존재하지 않는 사원코드이면 예외가 발생한다")
        void insertDepartmentFailEmployeeNotFound() {
            // given
            DepartmentCreateRequest request = new DepartmentCreateRequest(
                    null, "개발본부", "백엔드팀", "L1"
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.insertDepartment(request, "UNKNOWN")
            );
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("updateDepartment 메서드")
    class UpdateDepartment {

        @Test
        @DisplayName("부서명과 팀명이 모두 수정된다")
        void updateDepartmentBothNames() {
            // given
            DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                    1000L, "경영지원본부-수정", "시스템관리팀-수정"
            );

            Department existing = Department.builder()
                    .departmentId(1000L)
                    .departmentName("경영지원본부")
                    .teamName("시스템관리팀")
                    .depth("L0")
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(1000L))
                    .willReturn(Optional.of(existing));

            // when
            organizationManageCommandService.updateDepartment(request, "EMP-0001");

            // then
            assertEquals("경영지원본부-수정", existing.getDepartmentName());
            assertEquals("시스템관리팀-수정", existing.getTeamName());
        }

        @Test
        @DisplayName("부서명만 수정하면 팀명은 변경되지 않는다")
        void updateDepartmentOnlyDepartmentName() {
            // given
            DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                    1000L, "경영지원본부-수정", null
            );

            Department existing = Department.builder()
                    .departmentId(1000L)
                    .departmentName("경영지원본부")
                    .teamName("시스템관리팀")
                    .depth("L0")
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(1000L))
                    .willReturn(Optional.of(existing));

            // when
            organizationManageCommandService.updateDepartment(request, "EMP-0001");

            // then
            assertEquals("경영지원본부-수정", existing.getDepartmentName());
            assertEquals("시스템관리팀", existing.getTeamName());
        }

        @Test
        @DisplayName("팀명만 수정하면 부서명은 변경되지 않는다")
        void updateDepartmentOnlyTeamName() {
            // given
            DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                    1000L, null, "시스템관리팀-수정"
            );

            Department existing = Department.builder()
                    .departmentId(1000L)
                    .departmentName("경영지원본부")
                    .teamName("시스템관리팀")
                    .depth("L0")
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(1000L))
                    .willReturn(Optional.of(existing));

            // when
            organizationManageCommandService.updateDepartment(request, "EMP-0001");

            // then
            assertEquals("경영지원본부", existing.getDepartmentName());
            assertEquals("시스템관리팀-수정", existing.getTeamName());
        }

        @Test
        @DisplayName("존재하지 않는 부서이면 예외가 발생한다")
        void updateDepartmentNotFound() {
            // given
            DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                    9999L, "새부서명", null
            );

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(9999L))
                    .willReturn(Optional.empty());

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageCommandService.updateDepartment(request, "EMP-0001")
            );
            assertEquals("해당 부서를 찾을 수 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 사원코드이면 예외가 발생한다")
        void updateDepartmentEmployeeNotFound() {
            // given
            DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                    1000L, "새부서명", null
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.updateDepartment(request, "UNKNOWN")
            );
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteDepartment 메서드")
    class DeleteDepartment {

        @Test
        @DisplayName("부서가 소프트 삭제된다")
        void deleteDepartmentSuccess() {
            // given
            Department existing = Department.builder()
                    .departmentId(1000L)
                    .parentDepartmentId(50L)
                    .departmentName("경영지원본부")
                    .teamName("시스템관리팀")
                    .depth("L1")
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(1000L))
                    .willReturn(Optional.of(existing));

            // when
            organizationManageCommandService.deleteDepartment(1000L, "EMP-0001");

            // then
            assertEquals("삭제됨", existing.getDepartmentName());
            assertEquals("삭제됨", existing.getTeamName());
            assertNull(existing.getParentDepartmentId());
            assertNull(existing.getDepth());
        }

        @Test
        @DisplayName("존재하지 않는 부서이면 예외가 발생한다")
        void deleteDepartmentNotFound() {
            // given
            given(employeeRepository.findByEmployeeCode("EMP-0001"))
                    .willReturn(Optional.of(admin));
            given(departmentRepository.findById(9999L))
                    .willReturn(Optional.empty());

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageCommandService.deleteDepartment(9999L, "EMP-0001")
            );
            assertEquals("해당 부서를 찾을 수 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 사원코드이면 예외가 발생한다")
        void deleteDepartmentEmployeeNotFound() {
            // given
            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.deleteDepartment(1000L, "UNKNOWN")
            );
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
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
            organizationManageCommandService.insertEmployee(request, "EMP-0001");

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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageCommandService.insertEmployee(request, "EMP-0001")
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
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.insertEmployee(request, "UNKNOWN")
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
            organizationManageCommandService.updateEmployee(request, "EMP-0001");

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
            organizationManageCommandService.updateEmployee(request, "EMP-0001");

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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageCommandService.updateEmployee(request, "EMP-0001")
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
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.updateEmployee(request, "UNKNOWN")
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
            organizationManageCommandService.deleteEmployee("EMP2603001", "EMP-0001");

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
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageCommandService.deleteEmployee("UNKNOWN_TARGET", "EMP-0001")
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
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> organizationManageCommandService.deleteEmployee("EMP2603001", "UNKNOWN")
            );
            assertEquals("해당 사원 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }
}

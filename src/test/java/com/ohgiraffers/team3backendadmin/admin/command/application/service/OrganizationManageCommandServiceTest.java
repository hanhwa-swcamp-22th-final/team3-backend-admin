package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DepartmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
}

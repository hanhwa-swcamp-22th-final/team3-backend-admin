package com.ohgiraffers.team3backendadmin.admin.command.domain.service;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrganizationManageDomainServiceTest {

    @InjectMocks
    private OrganizationManageDomainService organizationManageDomainService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Nested
    @DisplayName("buildVerifiedDepartment 메서드")
    class BuildVerifiedDepartment {

        @Test
        @DisplayName("루트 부서일 때 depth=L0, teamName=null로 설정된다")
        void rootDepartment() {
            // given
            Department input = Department.builder()
                    .departmentId(1000L)
                    .parentDepartmentId(null)
                    .departmentName("경영지원본부")
                    .teamName("시스템관리팀")
                    .depth("anything")
                    .build();

            // when
            Department result = organizationManageDomainService.buildVerifiedDepartment(input);

            // then
            assertEquals(1000L, result.getDepartmentId());
            assertNull(result.getParentDepartmentId());
            assertEquals("경영지원본부", result.getDepartmentName());
            assertNull(result.getTeamName());
            assertEquals("L0", result.getDepth());
        }

        @Test
        @DisplayName("L0 부서 하위에 L1 부서가 생성된다")
        void childOfL0() {
            // given
            Department parent = Department.builder()
                    .departmentId(100L)
                    .depth("L0")
                    .build();

            Department input = Department.builder()
                    .departmentId(2000L)
                    .parentDepartmentId(100L)
                    .departmentName("개발본부")
                    .teamName("백엔드팀")
                    .depth("anything")
                    .build();

            given(departmentRepository.findById(100L)).willReturn(Optional.of(parent));

            // when
            Department result = organizationManageDomainService.buildVerifiedDepartment(input);

            // then
            assertEquals("L1", result.getDepth());
            assertEquals(100L, result.getParentDepartmentId());
            assertEquals("백엔드팀", result.getTeamName());
        }

        @Test
        @DisplayName("L2 부서 하위에 L3 부서가 생성된다")
        void childOfL2() {
            // given
            Department parent = Department.builder()
                    .departmentId(200L)
                    .depth("L2")
                    .build();

            Department input = Department.builder()
                    .departmentId(3000L)
                    .parentDepartmentId(200L)
                    .departmentName("세부팀")
                    .teamName("유닛A")
                    .build();

            given(departmentRepository.findById(200L)).willReturn(Optional.of(parent));

            // when
            Department result = organizationManageDomainService.buildVerifiedDepartment(input);

            // then
            assertEquals("L3", result.getDepth());
        }

        @Test
        @DisplayName("존재하지 않는 상위 부서이면 예외가 발생한다")
        void parentNotFound() {
            // given
            Department input = Department.builder()
                    .departmentId(3000L)
                    .parentDepartmentId(999L)
                    .departmentName("개발본부")
                    .build();

            given(departmentRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> organizationManageDomainService.buildVerifiedDepartment(input)
            );
            assertEquals("상위 부서를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("generateEmployeeCode 메서드")
    class GenerateEmployeeCode {

        @Test
        @DisplayName("기존 사원이 없으면 001부터 시작한다")
        void firstEmployeeOfMonth() {
            // given
            String prefix = "EMP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
            given(employeeRepository.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix))
                    .willReturn(Optional.empty());

            // when
            String code = organizationManageDomainService.generateEmployeeCode();

            // then
            assertEquals(prefix + "001", code);
        }

        @Test
        @DisplayName("기존 사원이 있으면 코드가 순차 증가한다")
        void incrementExistingCode() {
            // given
            String prefix = "EMP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
            Employee existing = Employee.builder()
                    .employeeCode(prefix + "005")
                    .build();

            given(employeeRepository.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix))
                    .willReturn(Optional.of(existing));

            // when
            String code = organizationManageDomainService.generateEmployeeCode();

            // then
            assertEquals(prefix + "006", code);
        }

        @Test
        @DisplayName("사원 코드 형식이 EMP + YY + MM + NNN이다")
        void codeFormatIsYYMM() {
            // given
            String prefix = "EMP" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
            given(employeeRepository.findTopByEmployeeCodeStartingWithOrderByEmployeeCodeDesc(prefix))
                    .willReturn(Optional.empty());

            // when
            String code = organizationManageDomainService.generateEmployeeCode();

            // then
            assertTrue(code.startsWith("EMP"));
            assertEquals(10, code.length()); // EMP(3) + YY(2) + MM(2) + NNN(3) = 10
        }
    }
}

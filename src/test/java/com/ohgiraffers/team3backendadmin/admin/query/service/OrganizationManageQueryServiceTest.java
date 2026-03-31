package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EmployeeResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EmployeeMapper;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrganizationManageQueryServiceTest {

    @InjectMocks
    private OrganizationManageQueryService organizationManageQueryService;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private AesEncryptor aesEncryptor;

    @Nested
    @DisplayName("getDepartmentById 메서드")
    class GetDepartmentById {

        @Test
        @DisplayName("부서 ID로 부서를 조회한다")
        void getDepartmentByIdSuccess() {
            // given
            DepartmentResponse response = new DepartmentResponse();
            given(departmentMapper.findById(1000L)).willReturn(response);

            // when
            DepartmentResponse result = organizationManageQueryService.getDepartmentById(1000L);

            // then
            assertNotNull(result);
            assertSame(response, result);
        }

        @Test
        @DisplayName("존재하지 않는 부서 ID이면 예외가 발생한다")
        void getDepartmentByIdNotFound() {
            // given
            given(departmentMapper.findById(9999L)).willReturn(null);

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageQueryService.getDepartmentById(9999L)
            );
            assertEquals("해당 부서를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getAllDepartments 메서드")
    class GetAllDepartments {

        @Test
        @DisplayName("전체 부서 목록을 조회한다")
        void getAllDepartmentsSuccess() {
            // given
            List<DepartmentResponse> departments = List.of(
                    new DepartmentResponse(),
                    new DepartmentResponse()
            );
            given(departmentMapper.findAll()).willReturn(departments);

            // when
            List<DepartmentResponse> result = organizationManageQueryService.getAllDepartments();

            // then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("부서가 없으면 빈 리스트를 반환한다")
        void getAllDepartmentsEmpty() {
            // given
            given(departmentMapper.findAll()).willReturn(List.of());

            // when
            List<DepartmentResponse> result = organizationManageQueryService.getAllDepartments();

            // then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getEmployeeByCode 메서드")
    class GetEmployeeByCode {

        @Test
        @DisplayName("사원코드로 사원을 조회하고 복호화한다")
        void getEmployeeByCodeSuccess() {
            // given
            EmployeeResponse response = new EmployeeResponse();
            response.setEmployeeCode("EMP001");
            response.setEmployeeEmail("ENCRYPTED_EMAIL");
            response.setEmployeePhone("ENCRYPTED_PHONE");
            response.setEmployeeAddress("ENCRYPTED_ADDR");
            response.setEmployeeEmergencyContact("ENCRYPTED_CONTACT");

            given(employeeMapper.findByEmployeeCode("EMP001")).willReturn(response);
            given(aesEncryptor.decrypt(anyString())).willAnswer(invocation -> "DECRYPTED_" + invocation.getArgument(0));

            // when
            EmployeeResponse result = organizationManageQueryService.getEmployeeByCode("EMP001");

            // then
            assertNotNull(result);
            assertEquals("DECRYPTED_ENCRYPTED_EMAIL", result.getEmployeeEmail());
            assertEquals("DECRYPTED_ENCRYPTED_PHONE", result.getEmployeePhone());
        }

        @Test
        @DisplayName("존재하지 않는 사원코드이면 예외가 발생한다")
        void getEmployeeByCodeNotFound() {
            // given
            given(employeeMapper.findByEmployeeCode("UNKNOWN")).willReturn(null);

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> organizationManageQueryService.getEmployeeByCode("UNKNOWN")
            );
            assertEquals("해당 사원을 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getAllEmployees 메서드")
    class GetAllEmployees {

        @Test
        @DisplayName("전체 사원 목록을 조회하고 복호화한다")
        void getAllEmployeesSuccess() {
            // given
            EmployeeResponse emp1 = new EmployeeResponse();
            emp1.setEmployeeEmail("E1");
            EmployeeResponse emp2 = new EmployeeResponse();
            emp2.setEmployeeEmail("E2");

            given(employeeMapper.findAll()).willReturn(List.of(emp1, emp2));
            given(aesEncryptor.decrypt(anyString())).willAnswer(invocation -> "D_" + invocation.getArgument(0));

            // when
            List<EmployeeResponse> result = organizationManageQueryService.getAllEmployees();

            // then
            assertEquals(2, result.size());
            assertEquals("D_E1", result.get(0).getEmployeeEmail());
            assertEquals("D_E2", result.get(1).getEmployeeEmail());
        }
    }
}

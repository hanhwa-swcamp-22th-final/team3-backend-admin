package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement.DepartmentManageQueryService;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DepartmentManageQueryServiceTest {

    @InjectMocks
    private DepartmentManageQueryService departmentManageQueryService;

    @Mock
    private DepartmentMapper departmentMapper;

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
            DepartmentResponse result = departmentManageQueryService.getDepartmentById(1000L);

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
            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> departmentManageQueryService.getDepartmentById(9999L)
            );
            assertEquals("해당 부서를 찾을 수 없습니다.", exception.getMessage());
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
            List<DepartmentResponse> result = departmentManageQueryService.getAllDepartments();

            // then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("부서가 없으면 빈 리스트를 반환한다")
        void getAllDepartmentsEmpty() {
            // given
            given(departmentMapper.findAll()).willReturn(List.of());

            // when
            List<DepartmentResponse> result = departmentManageQueryService.getAllDepartments();

            // then
            assertTrue(result.isEmpty());
        }
    }
}

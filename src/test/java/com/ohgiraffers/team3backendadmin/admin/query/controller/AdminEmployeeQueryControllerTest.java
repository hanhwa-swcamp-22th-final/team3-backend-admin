package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeProfileQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.EmployeeSkillQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee.TierChartPointQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.orgmanagement.EmployeeHrQueryService;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminEmployeeQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminEmployeeQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeHrQueryService employeeHrQueryService;

    @Test
    @DisplayName("Get worker profile API success")
    void getWorkerProfile_success() throws Exception {
        EmployeeProfileQueryResponse response = new EmployeeProfileQueryResponse();
        response.setEmployeeId(101L);
        response.setEmployeeCode("EMP-101");
        response.setEmployeeName("홍길동");
        response.setDepartmentName("생산본부");
        response.setTeamName("1팀");
        response.setCurrentTier(EmployeeTier.A);
        response.setTotalScore(new BigDecimal("88.50"));

        when(employeeHrQueryService.getProfile(101L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/admin/employees/101/profile"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.employeeId").value(101L))
            .andExpect(jsonPath("$.data.employeeName").value("홍길동"))
            .andExpect(jsonPath("$.data.currentTier").value("A"))
            .andExpect(jsonPath("$.data.totalScore").value(88.50));
    }

    @Test
    @DisplayName("Get worker profile API failure when not found")
    void getWorkerProfile_whenNotFound_thenReturn404() throws Exception {
        when(employeeHrQueryService.getProfile(999L))
            .thenThrow(new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND));

        mockMvc.perform(get("/api/v1/admin/employees/999/profile"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Get worker skills API success")
    void getWorkerSkills_success() throws Exception {
        EmployeeSkillQueryResponse response = new EmployeeSkillQueryResponse();
        response.setSkillId(301L);
        response.setSkillName("QUALITY_MANAGEMENT");
        response.setSkillScore(new BigDecimal("91.00"));

        when(employeeHrQueryService.getSkills(101L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/admin/employees/101/skills"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].skillId").value(301L))
            .andExpect(jsonPath("$.data[0].skillName").value("QUALITY_MANAGEMENT"))
            .andExpect(jsonPath("$.data[0].skillScore").value(91.00));
    }

    @Test
    @DisplayName("Get tier chart API success")
    void getTierChart_success() throws Exception {
        TierChartPointQueryResponse response = new TierChartPointQueryResponse();
        response.setYear(2025);
        response.setEvalSequence(2);
        response.setTier("A");
        response.setTotalScore(new BigDecimal("87.25"));

        when(employeeHrQueryService.getTierChart(101L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/admin/employees/101/tier-chart"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].year").value(2025))
            .andExpect(jsonPath("$.data[0].evalSequence").value(2))
            .andExpect(jsonPath("$.data[0].tier").value("A"))
            .andExpect(jsonPath("$.data[0].totalScore").value(87.25));
    }
}

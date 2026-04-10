package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeTierUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.EmployeeManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HRMEmployeeManageCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class HRMEmployeeManageCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeManageCommandService employeeManageCommandService;

    @Test
    @DisplayName("Update employee tier API success")
    void updateEmployeeTier_success() throws Exception {
        EmployeeTierUpdateRequest request = new EmployeeTierUpdateRequest();
        request.setTier(EmployeeTier.S);

        doNothing().when(employeeManageCommandService).updateEmployeeTier(101L, EmployeeTier.S);

        mockMvc.perform(patch("/api/v1/admin/employees/101/tier")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(employeeManageCommandService).updateEmployeeTier(101L, EmployeeTier.S);
    }
}

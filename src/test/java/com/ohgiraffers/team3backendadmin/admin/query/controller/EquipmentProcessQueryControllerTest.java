package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentManageQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentProcessQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FactoryLineQueryService factoryLineQueryService;

    @MockitoBean
    private EquipmentProcessQueryService equipmentProcessQueryService;

    @MockitoBean
    private EquipmentQueryService equipmentQueryService;

    @MockitoBean
    private EnvironmentStandardQueryService environmentStandardQueryService;

    @MockitoBean
    private EnvironmentEventQueryService environmentEventQueryService;

    @Test
    @DisplayName("Get equipment process list API success: return list JSON")
    void getEquipmentProcessList_success() throws Exception {
        EquipmentProcessQueryResponse response = new EquipmentProcessQueryResponse();
        response.setEquipmentProcessId(1L);
        response.setFactoryLineId(10L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");
        response.setEquipmentProcessCode("PROC-001");
        response.setEquipmentProcessName("Mixing Process");

        when(equipmentProcessQueryService.getEquipmentProcessList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentProcessId").value(1L))
            .andExpect(jsonPath("$.data[0].equipmentProcessCode").value("PROC-001"))
            .andExpect(jsonPath("$.data[0].equipmentProcessName").value("Mixing Process"));
    }

    @Test
    @DisplayName("Get equipment process list API success: bind query parameters correctly")
    void getEquipmentProcessList_withQueryParams_success() throws Exception {
        when(equipmentProcessQueryService.getEquipmentProcessList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(
                get("/api/v1/equipment-management/equipment-processes")
                    .param("factoryLineId", "10")
                    .param("keyword", "mix"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(equipmentProcessQueryService).getEquipmentProcessList(argThat(request ->
            Long.valueOf(10L).equals(request.getFactoryLineId())
                && "mix".equals(request.getKeyword())
        ));
    }

    @Test
    @DisplayName("Get equipment process detail API success: return detail JSON")
    void getEquipmentProcessDetail_success() throws Exception {
        EquipmentProcessDetailResponse response = new EquipmentProcessDetailResponse();
        response.setEquipmentProcessId(1L);
        response.setFactoryLineId(10L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");
        response.setEquipmentProcessCode("PROC-001");
        response.setEquipmentProcessName("Mixing Process");

        when(equipmentProcessQueryService.getEquipmentProcessDetail(1L))
            .thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(1L))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value("PROC-001"))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Mixing Process"));
    }

    @Test
    @DisplayName("Get equipment process detail API failure: propagate exception when the target does not exist")
    void getEquipmentProcessDetail_whenServiceThrowsNotFound_thenNotFound() {
        when(equipmentProcessQueryService.getEquipmentProcessDetail(999L))
            .thenThrow(new IllegalArgumentException("Equipment process not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/999")));
    }
}
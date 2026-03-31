package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
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
class FactoryLineQueryControllerTest {

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
    @DisplayName("Get factory line list API success: return list JSON")
    void getFactoryLineList_success() throws Exception {
        FactoryLineQueryResponse response = new FactoryLineQueryResponse();
        response.setFactoryLineId(1L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");

        when(factoryLineQueryService.getFactoryLineList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].factoryLineId").value(1L))
            .andExpect(jsonPath("$.data[0].factoryLineCode").value("LINE-001"))
            .andExpect(jsonPath("$.data[0].factoryLineName").value("Main Line"));
    }

    @Test
    @DisplayName("Get factory line list API success: bind query parameters correctly")
    void getFactoryLineList_withQueryParams_success() throws Exception {
        when(factoryLineQueryService.getFactoryLineList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines").param("keyword", "main"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(factoryLineQueryService).getFactoryLineList(argThat(request ->
            "main".equals(request.getKeyword())
        ));
    }

    @Test
    @DisplayName("Get factory line detail API success: return detail JSON")
    void getFactoryLineDetail_success() throws Exception {
        FactoryLineDetailResponse response = new FactoryLineDetailResponse();
        response.setFactoryLineId(1L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");

        when(factoryLineQueryService.getFactoryLineDetail(1L))
            .thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.factoryLineCode").value("LINE-001"))
            .andExpect(jsonPath("$.data.factoryLineName").value("Main Line"));
    }

    @Test
    @DisplayName("Get factory line detail API failure: propagate exception when the target does not exist")
    void getFactoryLineDetail_whenServiceThrowsNotFound_thenNotFound() {
        when(factoryLineQueryService.getFactoryLineDetail(999L))
            .thenThrow(new IllegalArgumentException("Factory line not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/factory-lines/999")));
    }
}
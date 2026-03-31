package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
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
class EnvironmentStandardQueryControllerTest {

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
    @DisplayName("Get environment standard list API success: return list JSON")
    void getEnvironmentStandardList_success() throws Exception {
        EnvironmentStandardQueryResponse response = new EnvironmentStandardQueryResponse();
        response.setEnvironmentStandardId(1L);
        response.setEnvironmentType(EnvironmentType.DRYROOM);
        response.setEnvironmentCode("ENV-001");
        response.setEnvironmentName("Dry Room");

        when(environmentStandardQueryService.getEnvironmentStandardList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentStandardId").value(1L))
            .andExpect(jsonPath("$.data[0].environmentType").value("DRYROOM"))
            .andExpect(jsonPath("$.data[0].environmentCode").value("ENV-001"));
    }

    @Test
    @DisplayName("Get environment standard list API success: bind query parameters correctly")
    void getEnvironmentStandardList_withQueryParams_success() throws Exception {
        when(environmentStandardQueryService.getEnvironmentStandardList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards")
                .param("keyword", "dry")
                .param("environmentType", "DRYROOM"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(environmentStandardQueryService).getEnvironmentStandardList(argThat(request ->
            "dry".equals(request.getKeyword()) && EnvironmentType.DRYROOM == request.getEnvironmentType()
        ));
    }

    @Test
    @DisplayName("Get environment standard detail API success: return detail JSON")
    void getEnvironmentStandardDetail_success() throws Exception {
        EnvironmentStandardDetailResponse response = new EnvironmentStandardDetailResponse();
        response.setEnvironmentStandardId(1L);
        response.setEnvironmentType(EnvironmentType.DRYROOM);
        response.setEnvironmentCode("ENV-001");
        response.setEnvironmentName("Dry Room");

        when(environmentStandardQueryService.getEnvironmentStandardDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(1L))
            .andExpect(jsonPath("$.data.environmentCode").value("ENV-001"));
    }

    @Test
    @DisplayName("Get environment standard detail failure: propagate service exception")
    void getEnvironmentStandardDetail_whenServiceThrows_thenPropagatesServletException() {
        when(environmentStandardQueryService.getEnvironmentStandardDetail(999L))
            .thenThrow(new IllegalArgumentException("Environment standard not found."));

        ServletException exception = assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/environment-standards/999")).andReturn());

        verify(environmentStandardQueryService).getEnvironmentStandardDetail(999L);
    }
}
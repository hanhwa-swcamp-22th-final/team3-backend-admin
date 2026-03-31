package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentManageCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class FactoryLineManageCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FactoryLineManageCommandService factoryLineManageCommandService;

    @MockitoBean
    private EquipmentProcessManageCommandService equipmentProcessManageCommandService;

    @MockitoBean
    private EquipmentManageCommandService equipmentManageCommandService;

    @Test
    @DisplayName("Create factory line API success: return created JSON")
    void createFactoryLine_success() throws Exception {
        FactoryLineCreateRequest request = FactoryLineCreateRequest.builder()
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        FactoryLineCreateResponse response = FactoryLineCreateResponse.builder()
            .factoryLineId(1L)
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        when(factoryLineManageCommandService.createFactoryLine(any(FactoryLineCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/admin/factory-lines")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.factoryLineCode").value("LINE-001"))
            .andExpect(jsonPath("$.data.factoryLineName").value("Main Line"));

        verify(factoryLineManageCommandService).createFactoryLine(any(FactoryLineCreateRequest.class));
    }

    @Test
    @DisplayName("Update factory line API success: return updated JSON")
    void updateFactoryLine_success() throws Exception {
        FactoryLineUpdateRequest request = FactoryLineUpdateRequest.builder()
            .factoryLineCode("LINE-999")
            .factoryLineName("Fixed Line")
            .build();

        FactoryLineUpdateResponse response = FactoryLineUpdateResponse.builder()
            .factoryLineId(1L)
            .factoryLineCode("LINE-999")
            .factoryLineName("Fixed Line")
            .build();

        when(factoryLineManageCommandService.updateFactoryLine(eq(1L), any(FactoryLineUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/admin/factory-lines/1")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.factoryLineCode").value("LINE-999"))
            .andExpect(jsonPath("$.data.factoryLineName").value("Fixed Line"));

        verify(factoryLineManageCommandService).updateFactoryLine(eq(1L), any(FactoryLineUpdateRequest.class));
    }

    @Test
    @DisplayName("Delete factory line API success: return soft-deleted JSON")
    void deleteFactoryLine_success() throws Exception {
        FactoryLineUpdateResponse response = FactoryLineUpdateResponse.builder()
            .factoryLineId(1L)
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        when(factoryLineManageCommandService.deleteFactoryLine(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/admin/factory-lines/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.factoryLineCode").value("LINE-001"))
            .andExpect(jsonPath("$.data.factoryLineName").value("Main Line"));

        verify(factoryLineManageCommandService).deleteFactoryLine(1L);
    }
}

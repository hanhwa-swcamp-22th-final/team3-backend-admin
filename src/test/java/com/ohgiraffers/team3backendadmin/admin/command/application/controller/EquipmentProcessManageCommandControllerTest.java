package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
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
class EquipmentProcessManageCommandControllerTest {

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
    @DisplayName("Create equipment process API success: return created JSON")
    void createEquipmentProcess_success() throws Exception {
        EquipmentProcessCreateRequest request = EquipmentProcessCreateRequest.builder()
            .factoryLineId(1L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        EquipmentProcessCreateResponse response = EquipmentProcessCreateResponse.builder()
            .equipmentProcessId(10L)
            .factoryLineId(1L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        when(equipmentProcessManageCommandService.createEquipmentProcess(any(EquipmentProcessCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/admin/equipment-processes")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(10L))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value("PROC-001"))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Mixing Process"));

        verify(equipmentProcessManageCommandService).createEquipmentProcess(any(EquipmentProcessCreateRequest.class));
    }

    @Test
    @DisplayName("Update equipment process API success: return updated JSON")
    void updateEquipmentProcess_success() throws Exception {
        EquipmentProcessUpdateRequest request = EquipmentProcessUpdateRequest.builder()
            .factoryLineId(2L)
            .equipmentProcessCode("PROC-999")
            .equipmentProcessName("Fixed Process")
            .build();

        EquipmentProcessUpdateResponse response = EquipmentProcessUpdateResponse.builder()
            .equipmentProcessId(10L)
            .factoryLineId(2L)
            .equipmentProcessCode("PROC-999")
            .equipmentProcessName("Fixed Process")
            .build();

        when(equipmentProcessManageCommandService.updateEquipmentProcess(eq(10L), any(EquipmentProcessUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/admin/equipment-processes/10")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(10L))
            .andExpect(jsonPath("$.data.factoryLineId").value(2L))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value("PROC-999"))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Fixed Process"));

        verify(equipmentProcessManageCommandService).updateEquipmentProcess(eq(10L), any(EquipmentProcessUpdateRequest.class));
    }

    @Test
    @DisplayName("Delete equipment process API success: return soft-deleted JSON")
    void deleteEquipmentProcess_success() throws Exception {
        EquipmentProcessUpdateResponse response = EquipmentProcessUpdateResponse.builder()
            .equipmentProcessId(10L)
            .factoryLineId(1L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        when(equipmentProcessManageCommandService.deleteEquipmentProcess(10L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/admin/equipment-processes/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(10L))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value("PROC-001"))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Mixing Process"));

        verify(equipmentProcessManageCommandService).deleteEquipmentProcess(10L);
    }
}

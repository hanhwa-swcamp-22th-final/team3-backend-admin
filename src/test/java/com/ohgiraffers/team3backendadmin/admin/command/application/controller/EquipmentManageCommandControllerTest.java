package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentManageCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentManageCommandControllerTest {

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
    @DisplayName("Create equipment API success: return a successful response")
    void createEquipment_success() throws Exception {
        EquipmentCreateRequest request = createEquipmentRequest();

        EquipmentCreateResponse response = EquipmentCreateResponse.builder()
            .equipmentId(4001L)
            .equipmentCode("EQ-001")
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .build();

        when(equipmentManageCommandService.createEquipment(any(EquipmentCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/admin/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(4001L))
            .andExpect(jsonPath("$.data.equipmentCode").value("EQ-001"))
            .andExpect(jsonPath("$.data.equipmentName").value("Printing Equipment"))
            .andExpect(jsonPath("$.data.equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data.equipmentGrade").value("S"));

        verify(equipmentManageCommandService).createEquipment(any(EquipmentCreateRequest.class));
    }

    @Test
    @DisplayName("Create equipment API failure: return 400 for invalid JSON")
    void createEquipment_whenInvalidRequest_thenBadRequest() throws Exception {
        String invalidJson = "{\"equipmentProcessId\":2001,\"equipmentCode\":";

        mockMvc.perform(post("/api/v1/admin/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update equipment API success: return a successful response")
    void updateEquipment_success() throws Exception {
        EquipmentUpdateRequest request = createEquipmentUpdateRequest();

        doNothing().when(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));

        mockMvc.perform(put("/api/v1/admin/equipments/4001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());

        verify(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));
    }

    @Test
    @DisplayName("Update equipment API failure: propagate service exception")
    void updateEquipment_whenServiceThrows_thenPropagatesServletException() throws Exception {
        EquipmentUpdateRequest request = createEquipmentUpdateRequest();

        doThrow(new IllegalArgumentException("Equipment not found."))
            .when(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));

        ServletException exception = assertThrows(ServletException.class,
            () -> mockMvc.perform(put("/api/v1/admin/equipments/4001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))));

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Equipment not found.", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Delete equipment API success: return a successful response")
    void deleteEquipment_success() throws Exception {
        doNothing().when(equipmentManageCommandService).deleteEquipment(4001L);

        mockMvc.perform(delete("/api/v1/admin/equipments/4001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());

        verify(equipmentManageCommandService).deleteEquipment(4001L);
    }

    private EquipmentCreateRequest createEquipmentRequest() {
        return EquipmentCreateRequest.builder()
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-001")
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .equipmentDescription("New equipment")
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.75)
            .build();
    }

    private EquipmentUpdateRequest createEquipmentUpdateRequest() {
        return EquipmentUpdateRequest.builder()
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-002")
            .equipmentName("Updated Equipment")
            .equipmentStatus(EquipmentStatus.STOPPED)
            .equipmentGrade(EquipmentGrade.B)
            .equipmentDescription("Updated description")
            .equipmentWarrantyMonth(60)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.9)
            .build();
    }
}

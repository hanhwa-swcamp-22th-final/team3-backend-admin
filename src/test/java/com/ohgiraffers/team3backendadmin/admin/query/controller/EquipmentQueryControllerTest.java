package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
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

import java.time.LocalDate;
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
class EquipmentQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FactoryLineQueryService factoryLineQueryService;

    @MockitoBean
    private EquipmentProcessQueryService equipmentProcessQueryService;

    @MockitoBean
    private EquipmentQueryService equipmentQueryService;

    @Test
    @DisplayName("Get equipment list API success: return list JSON")
    void getEquipments_success() throws Exception {
        EquipmentQueryResponse response = new EquipmentQueryResponse();
        response.setEquipmentId(1L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Printer");
        response.setEquipmentStatus(EquipmentStatus.OPERATING);
        response.setEquipmentGrade(EquipmentGrade.S);

        when(equipmentQueryService.getEquipmentList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/admin/equipments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentId").value(1L))
            .andExpect(jsonPath("$.data[0].equipmentCode").value("EQ-001"))
            .andExpect(jsonPath("$.data[0].equipmentName").value("Printer"))
            .andExpect(jsonPath("$.data[0].equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data[0].equipmentGrade").value("S"));
    }

    @Test
    @DisplayName("Get equipment list API success: bind query parameters correctly")
    void getEquipments_withQueryParams_success() throws Exception {
        when(equipmentQueryService.getEquipmentList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(
                get("/api/v1/admin/equipments")
                    .param("keyword", "printer")
                    .param("equipmentStatus", "OPERATING")
                    .param("equipmentGrade", "S")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(equipmentQueryService).getEquipmentList(argThat(request ->
            "printer".equals(request.getKeyword())
                && request.getEquipmentStatus() == EquipmentStatus.OPERATING
                && request.getEquipmentGrade() == EquipmentGrade.S
        ));
    }

    @Test
    @DisplayName("Get equipment detail API success: return detail JSON")
    void getEquipmentDetail_success() throws Exception {
        EquipmentDetailResponse response = new EquipmentDetailResponse();
        response.setEquipmentId(1L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Printer");
        response.setEquipmentStatus(EquipmentStatus.OPERATING);
        response.setEquipmentGrade(EquipmentGrade.S);
        response.setEquipmentInstallDate(LocalDate.of(2025, 1, 15).atStartOfDay());
        response.setEquipmentDescription("Main line printer");

        when(equipmentQueryService.getEquipmentDetail(1L))
            .thenReturn(response);

        mockMvc.perform(get("/api/v1/admin/equipments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(1L))
            .andExpect(jsonPath("$.data.equipmentCode").value("EQ-001"))
            .andExpect(jsonPath("$.data.equipmentName").value("Printer"))
            .andExpect(jsonPath("$.data.equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data.equipmentGrade").value("S"))
            .andExpect(jsonPath("$.data.equipmentDescription").value("Main line printer"));
    }

    @Test
    @DisplayName("Get equipment detail API failure: propagate exception when the target does not exist")
    void getEquipmentDetail_whenServiceThrowsNotFound_thenNotFound() {
        when(equipmentQueryService.getEquipmentDetail(999L))
            .thenThrow(new IllegalArgumentException("Equipment not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/admin/equipments/999")));
    }
}
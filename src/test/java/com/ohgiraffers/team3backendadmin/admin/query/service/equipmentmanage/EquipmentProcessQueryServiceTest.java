package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentProcessQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentProcessQueryServiceTest {

    @Mock
    private EquipmentProcessQueryMapper equipmentProcessQueryMapper;

    @InjectMocks
    private EquipmentProcessQueryService equipmentProcessQueryService;

    private EquipmentProcessSearchRequest searchRequest;
    private EquipmentProcessQueryResponse equipmentProcessQueryResponse;
    private EquipmentProcessDetailResponse equipmentProcessDetailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = EquipmentProcessSearchRequest.builder()
            .factoryLineId(1L)
            .keyword("mix")
            .build();

        equipmentProcessQueryResponse = new EquipmentProcessQueryResponse();
        equipmentProcessQueryResponse.setEquipmentProcessId(1L);
        equipmentProcessQueryResponse.setFactoryLineId(10L);
        equipmentProcessQueryResponse.setFactoryLineCode("LINE-001");
        equipmentProcessQueryResponse.setFactoryLineName("Main Line");
        equipmentProcessQueryResponse.setEquipmentProcessCode("PROC-001");
        equipmentProcessQueryResponse.setEquipmentProcessName("Mixing Process");

        equipmentProcessDetailResponse = new EquipmentProcessDetailResponse();
        equipmentProcessDetailResponse.setEquipmentProcessId(1L);
        equipmentProcessDetailResponse.setFactoryLineId(10L);
        equipmentProcessDetailResponse.setFactoryLineCode("LINE-001");
        equipmentProcessDetailResponse.setFactoryLineName("Main Line");
        equipmentProcessDetailResponse.setEquipmentProcessCode("PROC-001");
        equipmentProcessDetailResponse.setEquipmentProcessName("Mixing Process");
    }

    @Test
    @DisplayName("Get equipment process list success: return a list response DTO")
    void getEquipmentProcessList_success() {
        when(equipmentProcessQueryMapper.selectEquipmentProcessList(searchRequest)).thenReturn(List.of(equipmentProcessQueryResponse));

        List<EquipmentProcessQueryResponse> result = equipmentProcessQueryService.getEquipmentProcessList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEquipmentProcessId());
        assertEquals("PROC-001", result.get(0).getEquipmentProcessCode());
        assertEquals("Mixing Process", result.get(0).getEquipmentProcessName());
        verify(equipmentProcessQueryMapper).selectEquipmentProcessList(searchRequest);
    }

    @Test
    @DisplayName("Get equipment process list failure: return an empty list when there is no data")
    void getEquipmentProcessList_whenNoData_thenReturnEmptyList() {
        EquipmentProcessSearchRequest emptyRequest = EquipmentProcessSearchRequest.builder().build();
        when(equipmentProcessQueryMapper.selectEquipmentProcessList(emptyRequest)).thenReturn(List.of());

        List<EquipmentProcessQueryResponse> result = equipmentProcessQueryService.getEquipmentProcessList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(equipmentProcessQueryMapper).selectEquipmentProcessList(emptyRequest);
    }

    @Test
    @DisplayName("Get equipment process detail success: return a detail response DTO")
    void getEquipmentProcessDetail_success() {
        when(equipmentProcessQueryMapper.selectEquipmentProcessDetailById(1L)).thenReturn(equipmentProcessDetailResponse);

        EquipmentProcessDetailResponse result = equipmentProcessQueryService.getEquipmentProcessDetail(1L);

        assertEquals(1L, result.getEquipmentProcessId());
        assertEquals("PROC-001", result.getEquipmentProcessCode());
        assertEquals("Mixing Process", result.getEquipmentProcessName());
        verify(equipmentProcessQueryMapper).selectEquipmentProcessDetailById(1L);
    }

    @Test
    @DisplayName("Get equipment process detail failure: throw business exception when equipment process is not found")
    void getEquipmentProcessDetail_whenNotFound_thenThrow() {
        when(equipmentProcessQueryMapper.selectEquipmentProcessDetailById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentProcessQueryService.getEquipmentProcessDetail(999L)
        );

        assertEquals(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND, exception.getErrorCode());
        assertEquals("해당 공정을 찾을 수 없습니다.", exception.getMessage());
        verify(equipmentProcessQueryMapper).selectEquipmentProcessDetailById(999L);
    }

    @Test
    @DisplayName("Check equipment process code existence: return true when the code exists")
    void existsByEquipmentProcessCode_whenExists_thenTrue() {
        when(equipmentProcessQueryMapper.selectEquipmentProcessIdByCode("PROC-001")).thenReturn(1L);

        boolean result = equipmentProcessQueryService.existsByEquipmentProcessCode("PROC-001");

        assertTrue(result);
        verify(equipmentProcessQueryMapper).selectEquipmentProcessIdByCode("PROC-001");
    }

    @Test
    @DisplayName("Check equipment process code existence: return false when the code does not exist")
    void existsByEquipmentProcessCode_whenNotExists_thenFalse() {
        when(equipmentProcessQueryMapper.selectEquipmentProcessIdByCode("PROC-404")).thenReturn(null);

        boolean result = equipmentProcessQueryService.existsByEquipmentProcessCode("PROC-404");

        org.junit.jupiter.api.Assertions.assertFalse(result);
        verify(equipmentProcessQueryMapper).selectEquipmentProcessIdByCode("PROC-404");
    }
}

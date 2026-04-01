package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentQueryServiceTest {

    @Mock
    private EquipmentQueryMapper equipmentQueryMapper;

    @InjectMocks
    private EquipmentQueryService equipmentQueryService;

    private EquipmentSearchRequest searchRequest;
    private EquipmentQueryResponse equipmentQueryResponse;
    private EquipmentLatestSnapshotQueryResponse equipmentLatestSnapshotQueryResponse;
    private EquipmentDetailResponse equipmentDetailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = EquipmentSearchRequest.builder()
            .keyword("printer")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .build();

        equipmentQueryResponse = new EquipmentQueryResponse();
        equipmentQueryResponse.setEquipmentId(1L);
        equipmentQueryResponse.setEquipmentCode("EQ-001");
        equipmentQueryResponse.setEquipmentName("Printer");
        equipmentQueryResponse.setEquipmentStatus(EquipmentStatus.OPERATING);
        equipmentQueryResponse.setEquipmentGrade(EquipmentGrade.S);

        equipmentLatestSnapshotQueryResponse = new EquipmentLatestSnapshotQueryResponse();
        equipmentLatestSnapshotQueryResponse.setEquipmentId(1L);
        equipmentLatestSnapshotQueryResponse.setEquipmentCode("EQ-001");
        equipmentLatestSnapshotQueryResponse.setEquipmentName("Printer");

        equipmentDetailResponse = new EquipmentDetailResponse();
        equipmentDetailResponse.setEquipmentId(1L);
        equipmentDetailResponse.setEquipmentCode("EQ-001");
        equipmentDetailResponse.setEquipmentName("Printer");
        equipmentDetailResponse.setEquipmentStatus(EquipmentStatus.OPERATING);
        equipmentDetailResponse.setEquipmentGrade(EquipmentGrade.S);
        equipmentDetailResponse.setEquipmentDescription("Main line printer");
    }

    @Test
    @DisplayName("Get equipment list success: return a list response DTO")
    void getEquipments_success() {
        when(equipmentQueryMapper.selectEquipmentList(searchRequest)).thenReturn(List.of(equipmentQueryResponse));

        List<EquipmentQueryResponse> result = equipmentQueryService.getEquipmentList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEquipmentId());
        assertEquals("EQ-001", result.get(0).getEquipmentCode());
        assertEquals("Printer", result.get(0).getEquipmentName());
        verify(equipmentQueryMapper).selectEquipmentList(searchRequest);
    }

    @Test
    @DisplayName("Get equipment list failure: return an empty list when there is no data")
    void getEquipments_whenNoData_thenReturnEmptyList() {
        EquipmentSearchRequest emptyRequest = EquipmentSearchRequest.builder().build();
        when(equipmentQueryMapper.selectEquipmentList(emptyRequest)).thenReturn(List.of());

        List<EquipmentQueryResponse> result = equipmentQueryService.getEquipmentList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(equipmentQueryMapper).selectEquipmentList(emptyRequest);
    }

    @Test
    @DisplayName("Get equipment list with latest snapshots success")
    void getEquipmentListWithLatestSnapshots_success() {
        when(equipmentQueryMapper.selectEquipmentListWithLatestSnapshots(searchRequest))
            .thenReturn(List.of(equipmentLatestSnapshotQueryResponse));

        List<EquipmentLatestSnapshotQueryResponse> result = equipmentQueryService.getEquipmentListWithLatestSnapshots(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEquipmentId());
        verify(equipmentQueryMapper).selectEquipmentListWithLatestSnapshots(searchRequest);
    }

    @Test
    @DisplayName("Get equipment detail success: return a detail response DTO")
    void getEquipmentDetail_success() {
        when(equipmentQueryMapper.selectEquipmentDetailById(1L)).thenReturn(equipmentDetailResponse);

        EquipmentDetailResponse result = equipmentQueryService.getEquipmentDetail(1L);

        assertEquals(1L, result.getEquipmentId());
        assertEquals("EQ-001", result.getEquipmentCode());
        assertEquals("Printer", result.getEquipmentName());
        verify(equipmentQueryMapper).selectEquipmentDetailById(1L);
    }

    @Test
    @DisplayName("Get equipment detail failure: throw business exception when equipment is not found")
    void getEquipmentDetail_whenEquipmentNotFound_thenThrow() {
        when(equipmentQueryMapper.selectEquipmentDetailById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentQueryService.getEquipmentDetail(999L)
        );

        assertEquals(ErrorCode.EQUIPMENT_NOT_FOUND, exception.getErrorCode());
        assertEquals("해당 설비를 찾을 수 없습니다.", exception.getMessage());
        verify(equipmentQueryMapper).selectEquipmentDetailById(999L);
    }

    @Test
    @DisplayName("Check equipment code existence: return true when the code exists")
    void existsByEquipmentCode_whenExists_thenTrue() {
        when(equipmentQueryMapper.selectEquipmentIdByCode("EQ-001")).thenReturn(1L);

        boolean result = equipmentQueryService.existsByEquipmentCode("EQ-001");

        assertTrue(result);
        verify(equipmentQueryMapper).selectEquipmentIdByCode("EQ-001");
    }

    @Test
    @DisplayName("Check equipment code existence: return false when the code does not exist")
    void existsByEquipmentCode_whenNotExists_thenFalse() {
        when(equipmentQueryMapper.selectEquipmentIdByCode("EQ-404")).thenReturn(null);

        boolean result = equipmentQueryService.existsByEquipmentCode("EQ-404");

        assertFalse(result);
        verify(equipmentQueryMapper).selectEquipmentIdByCode("EQ-404");
    }

    @Test
    @DisplayName("Get aging parameter ID success: return mapped ID")
    void getEquipmentAgingParamIdByEquipmentId_success() {
        when(equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(1L)).thenReturn(101L);

        Long result = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(1L);

        assertEquals(101L, result);
        verify(equipmentQueryMapper).selectEquipmentAgingParamIdByEquipmentId(1L);
    }

    @Test
    @DisplayName("Get baseline ID success: return mapped ID")
    void getEquipmentBaselineIdByEquipmentId_success() {
        when(equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(1L)).thenReturn(201L);

        Long result = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(1L);

        assertEquals(201L, result);
        verify(equipmentQueryMapper).selectEquipmentBaselineIdByEquipmentId(1L);
    }
}

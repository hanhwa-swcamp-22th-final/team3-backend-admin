package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentEventQueryMapper;
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
class EnvironmentEventQueryServiceTest {

    @Mock
    private EnvironmentEventQueryMapper environmentEventQueryMapper;

    @InjectMocks
    private EnvironmentEventQueryService environmentEventQueryService;

    private EnvironmentEventSearchRequest searchRequest;
    private EnvironmentEventQueryResponse environmentEventQueryResponse;
    private EnvironmentEventDetailResponse environmentEventDetailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = EnvironmentEventSearchRequest.builder()
            .equipmentId(1L)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .build();

        environmentEventQueryResponse = new EnvironmentEventQueryResponse();
        environmentEventQueryResponse.setEnvironmentEventId(1L);
        environmentEventQueryResponse.setEquipmentId(10L);
        environmentEventQueryResponse.setEquipmentCode("EQ-001");
        environmentEventQueryResponse.setEquipmentName("Drying Equipment");
        environmentEventQueryResponse.setEnvDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION);
        environmentEventQueryResponse.setEnvCorrectionApplied(false);

        environmentEventDetailResponse = new EnvironmentEventDetailResponse();
        environmentEventDetailResponse.setEnvironmentEventId(1L);
        environmentEventDetailResponse.setEquipmentId(10L);
        environmentEventDetailResponse.setEquipmentCode("EQ-001");
        environmentEventDetailResponse.setEquipmentName("Drying Equipment");
        environmentEventDetailResponse.setEnvDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION);
        environmentEventDetailResponse.setEnvCorrectionApplied(false);
    }

    @Test
    @DisplayName("Get environment event list success: return a list response DTO")
    void getEnvironmentEventList_success() {
        when(environmentEventQueryMapper.selectEnvironmentEventList(searchRequest))
            .thenReturn(List.of(environmentEventQueryResponse));

        List<EnvironmentEventQueryResponse> result = environmentEventQueryService.getEnvironmentEventList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEnvironmentEventId());
        assertEquals(10L, result.get(0).getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, result.get(0).getEnvDeviationType());
        verify(environmentEventQueryMapper).selectEnvironmentEventList(searchRequest);
    }

    @Test
    @DisplayName("Get environment event list failure: return an empty list when there is no data")
    void getEnvironmentEventList_whenNoData_thenReturnEmptyList() {
        EnvironmentEventSearchRequest emptyRequest = EnvironmentEventSearchRequest.builder().build();
        when(environmentEventQueryMapper.selectEnvironmentEventList(emptyRequest)).thenReturn(List.of());

        List<EnvironmentEventQueryResponse> result = environmentEventQueryService.getEnvironmentEventList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(environmentEventQueryMapper).selectEnvironmentEventList(emptyRequest);
    }

    @Test
    @DisplayName("Get environment event detail success: return a detail response DTO")
    void getEnvironmentEventDetail_success() {
        when(environmentEventQueryMapper.selectEnvironmentEventDetailById(1L)).thenReturn(environmentEventDetailResponse);

        EnvironmentEventDetailResponse result = environmentEventQueryService.getEnvironmentEventDetail(1L);

        assertEquals(1L, result.getEnvironmentEventId());
        assertEquals(10L, result.getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, result.getEnvDeviationType());
        verify(environmentEventQueryMapper).selectEnvironmentEventDetailById(1L);
    }

    @Test
    @DisplayName("Get environment event detail failure: throw business exception when environment event is not found")
    void getEnvironmentEventDetail_whenNotFound_thenThrow() {
        when(environmentEventQueryMapper.selectEnvironmentEventDetailById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> environmentEventQueryService.getEnvironmentEventDetail(999L)
        );

        assertEquals(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND, exception.getErrorCode());
        assertEquals("해당 환경 이벤트를 찾을 수 없습니다.", exception.getMessage());
        verify(environmentEventQueryMapper).selectEnvironmentEventDetailById(999L);
    }
}

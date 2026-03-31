package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardUpdateResponse;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentStandardManageCommandServiceTest {

    @Mock
    private EnvironmentStandardRepository environmentStandardRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private EnvironmentStandardManageCommandService environmentStandardManageCommandService;

    private EnvironmentStandard environmentStandard;
    private EnvironmentStandardCreateRequest createRequest;
    private EnvironmentStandardUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        environmentStandard = EnvironmentStandard.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .envTempMin(new BigDecimal("18.0"))
            .envTempMax(new BigDecimal("25.0"))
            .envHumidityMin(new BigDecimal("30.0"))
            .envHumidityMax(new BigDecimal("40.0"))
            .envParticleLimit(100)
            .isDeleted(false)
            .build();

        createRequest = EnvironmentStandardCreateRequest.builder()
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .envTempMin(new BigDecimal("18.0"))
            .envTempMax(new BigDecimal("25.0"))
            .envHumidityMin(new BigDecimal("30.0"))
            .envHumidityMax(new BigDecimal("40.0"))
            .envParticleLimit(100)
            .build();

        updateRequest = EnvironmentStandardUpdateRequest.builder()
            .environmentType(EnvironmentType.CLEANROOM)
            .environmentCode("ENV-999")
            .environmentName("Clean Room")
            .envTempMin(new BigDecimal("20.0"))
            .envTempMax(new BigDecimal("24.0"))
            .envHumidityMin(new BigDecimal("35.0"))
            .envHumidityMax(new BigDecimal("45.0"))
            .envParticleLimit(80)
            .build();
    }

    @Test
    @DisplayName("Create environment standard success: save environment standard from request")
    void createEnvironmentStandard_success() {
        when(environmentStandardRepository.findByEnvironmentCode("ENV-001"))
            .thenReturn(Optional.empty());
        when(idGenerator.generate())
            .thenReturn(3001L);
        when(environmentStandardRepository.save(any(EnvironmentStandard.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EnvironmentStandardCreateResponse response = environmentStandardManageCommandService.createEnvironmentStandard(createRequest);

        ArgumentCaptor<EnvironmentStandard> captor = ArgumentCaptor.forClass(EnvironmentStandard.class);
        verify(environmentStandardRepository).save(captor.capture());

        EnvironmentStandard savedEnvironmentStandard = captor.getValue();
        assertEquals(3001L, savedEnvironmentStandard.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, savedEnvironmentStandard.getEnvironmentType());
        assertEquals("ENV-001", savedEnvironmentStandard.getEnvironmentCode());
        assertEquals("Dry Room", savedEnvironmentStandard.getEnvironmentName());
        assertFalse(savedEnvironmentStandard.getIsDeleted());

        assertEquals(3001L, response.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, response.getEnvironmentType());
        assertEquals("ENV-001", response.getEnvironmentCode());
        assertEquals("Dry Room", response.getEnvironmentName());
    }

    @Test
    @DisplayName("Create environment standard failure: throw exception when code already exists")
    void createEnvironmentStandard_whenCodeAlreadyExists_thenThrow() {
        when(environmentStandardRepository.findByEnvironmentCode("ENV-001"))
            .thenReturn(Optional.of(environmentStandard));

        assertThrows(IllegalArgumentException.class,
            () -> environmentStandardManageCommandService.createEnvironmentStandard(createRequest));

        verify(environmentStandardRepository, never()).save(any(EnvironmentStandard.class));
        verify(idGenerator, never()).generate();
    }

    @Test
    @DisplayName("Update environment standard success: update existing environment standard")
    void updateEnvironmentStandard_success() {
        when(environmentStandardRepository.findById(3001L))
            .thenReturn(Optional.of(environmentStandard));

        EnvironmentStandardUpdateResponse response = environmentStandardManageCommandService.updateEnvironmentStandard(3001L, updateRequest);

        assertEquals(EnvironmentType.CLEANROOM, environmentStandard.getEnvironmentType());
        assertEquals("ENV-999", environmentStandard.getEnvironmentCode());
        assertEquals("Clean Room", environmentStandard.getEnvironmentName());
        assertEquals(3001L, response.getEnvironmentStandardId());
        assertEquals(EnvironmentType.CLEANROOM, response.getEnvironmentType());
        assertEquals("ENV-999", response.getEnvironmentCode());
        assertEquals("Clean Room", response.getEnvironmentName());
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when environment standard does not exist")
    void updateEnvironmentStandard_whenNotFound_thenThrow() {
        when(environmentStandardRepository.findById(9999L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> environmentStandardManageCommandService.updateEnvironmentStandard(9999L, updateRequest));
    }

    @Test
    @DisplayName("Delete environment standard success: soft delete existing environment standard")
    void deleteEnvironmentStandard_success() {
        when(environmentStandardRepository.findById(3001L))
            .thenReturn(Optional.of(environmentStandard));

        EnvironmentStandardUpdateResponse response = environmentStandardManageCommandService.deleteEnvironmentStandard(3001L);

        assertTrue(environmentStandard.getIsDeleted());
        assertEquals(3001L, response.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, response.getEnvironmentType());
        assertEquals("ENV-001", response.getEnvironmentCode());
        assertEquals("Dry Room", response.getEnvironmentName());
    }

    @Test
    @DisplayName("Delete environment standard failure: throw exception when environment standard does not exist")
    void deleteEnvironmentStandard_whenNotFound_thenThrow() {
        when(environmentStandardRepository.findById(9999L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> environmentStandardManageCommandService.deleteEnvironmentStandard(9999L));
    }
}

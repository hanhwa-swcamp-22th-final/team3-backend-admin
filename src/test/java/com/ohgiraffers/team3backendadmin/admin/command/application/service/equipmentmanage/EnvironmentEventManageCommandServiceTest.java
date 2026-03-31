package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventUpdateResponse;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentEventRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentEventManageCommandServiceTest {

    @Mock
    private EnvironmentEventRepository environmentEventRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private EnvironmentEventManageCommandService environmentEventManageCommandService;

    private Equipment equipment;
    private EnvironmentEvent environmentEvent;
    private EnvironmentEventCreateRequest createRequest;
    private EnvironmentEventUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
            .equipmentId(4001L)
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-001")
            .equipmentName("Drying Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Dry room equipment")
            .build();

        environmentEvent = EnvironmentEvent.builder()
            .environmentEventId(5001L)
            .equipmentId(4001L)
            .envTemperature(new BigDecimal("24.0"))
            .envHumidity(new BigDecimal("38.0"))
            .envParticleCnt(90)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
            .build();

        createRequest = EnvironmentEventCreateRequest.builder()
            .equipmentId(4001L)
            .envTemperature(new BigDecimal("24.0"))
            .envHumidity(new BigDecimal("38.0"))
            .envParticleCnt(90)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
            .build();

        updateRequest = EnvironmentEventUpdateRequest.builder()
            .equipmentId(4001L)
            .envTemperature(new BigDecimal("26.0"))
            .envHumidity(new BigDecimal("42.0"))
            .envParticleCnt(110)
            .envDeviationType(EnvDeviationType.HUMIDITY_DEVIATION)
            .envCorrectionApplied(true)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 10, 0))
            .build();
    }

    @Test
    @DisplayName("Create environment event success: save environment event from request")
    void createEnvironmentEvent_success() {
        when(equipmentRepository.findById(4001L))
            .thenReturn(Optional.of(equipment));
        when(idGenerator.generate())
            .thenReturn(5001L);
        when(environmentEventRepository.save(any(EnvironmentEvent.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EnvironmentEventCreateResponse response = environmentEventManageCommandService.createEnvironmentEvent(createRequest);

        ArgumentCaptor<EnvironmentEvent> captor = ArgumentCaptor.forClass(EnvironmentEvent.class);
        verify(environmentEventRepository).save(captor.capture());

        EnvironmentEvent savedEnvironmentEvent = captor.getValue();
        assertEquals(5001L, savedEnvironmentEvent.getEnvironmentEventId());
        assertEquals(4001L, savedEnvironmentEvent.getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, savedEnvironmentEvent.getEnvDeviationType());
        assertFalse(savedEnvironmentEvent.getEnvCorrectionApplied());

        assertEquals(5001L, response.getEnvironmentEventId());
        assertEquals(4001L, response.getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, response.getEnvDeviationType());
        assertFalse(response.getEnvCorrectionApplied());
    }

    @Test
    @DisplayName("Create environment event failure: throw exception when equipment does not exist")
    void createEnvironmentEvent_whenEquipmentNotFound_thenThrow() {
        when(equipmentRepository.findById(4001L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> environmentEventManageCommandService.createEnvironmentEvent(createRequest));

        verify(environmentEventRepository, never()).save(any(EnvironmentEvent.class));
        verify(idGenerator, never()).generate();
    }

    @Test
    @DisplayName("Update environment event success: update existing environment event")
    void updateEnvironmentEvent_success() {
        when(environmentEventRepository.findById(5001L))
            .thenReturn(Optional.of(environmentEvent));
        when(equipmentRepository.findById(4001L))
            .thenReturn(Optional.of(equipment));

        EnvironmentEventUpdateResponse response = environmentEventManageCommandService.updateEnvironmentEvent(5001L, updateRequest);

        assertEquals(4001L, environmentEvent.getEquipmentId());
        assertEquals(EnvDeviationType.HUMIDITY_DEVIATION, environmentEvent.getEnvDeviationType());
        assertTrue(environmentEvent.getEnvCorrectionApplied());
        assertEquals(5001L, response.getEnvironmentEventId());
        assertEquals(4001L, response.getEquipmentId());
        assertEquals(EnvDeviationType.HUMIDITY_DEVIATION, response.getEnvDeviationType());
        assertTrue(response.getEnvCorrectionApplied());
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when environment event does not exist")
    void updateEnvironmentEvent_whenNotFound_thenThrow() {
        when(environmentEventRepository.findById(9999L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> environmentEventManageCommandService.updateEnvironmentEvent(9999L, updateRequest));
    }

    @Test
    @DisplayName("Delete environment event success: delete existing environment event")
    void deleteEnvironmentEvent_success() {
        when(environmentEventRepository.findById(5001L))
            .thenReturn(Optional.of(environmentEvent));

        EnvironmentEventUpdateResponse response = environmentEventManageCommandService.deleteEnvironmentEvent(5001L);

        verify(environmentEventRepository).delete(environmentEvent);
        assertEquals(5001L, response.getEnvironmentEventId());
        assertEquals(4001L, response.getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, response.getEnvDeviationType());
        assertFalse(response.getEnvCorrectionApplied());
    }

    @Test
    @DisplayName("Delete environment event failure: throw exception when environment event does not exist")
    void deleteEnvironmentEvent_whenNotFound_thenThrow() {
        when(environmentEventRepository.findById(9999L))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> environmentEventManageCommandService.deleteEnvironmentEvent(9999L));
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentManageCommandServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentProcessRepository equipmentProcessRepository;

    @Mock
    private EnvironmentStandardRepository environmentStandardRepository;

    @Mock
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Mock
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Mock
    private EquipmentQueryService equipmentQueryService;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private EquipmentManageCommandService equipmentCommandService;

    private FactoryLine factoryLine;
    private EquipmentProcess equipmentProcess;
    private EnvironmentStandard environmentStandard;
    private EquipmentCreateRequest equipmentCreateRequest;
    private EquipmentUpdateRequest equipmentUpdateRequest;
    private Equipment existingEquipment;
    private EquipmentAgingParam existingEquipmentAgingParam;

    @BeforeEach
    void setUp() {
        factoryLine = new FactoryLine(1001L, "LINE-01", "Line 1");

        equipmentProcess = new EquipmentProcess(2001L, factoryLine.getFactoryLineId(), "PROC-01", "Drying Process");

        environmentStandard = EnvironmentStandard.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("DRY-ENV-001")
            .environmentName("Dry Room 001")
            .envTempMin(new BigDecimal("20.0"))
            .envTempMax(new BigDecimal("25.0"))
            .envHumidityMin(new BigDecimal("30.0"))
            .envHumidityMax(new BigDecimal("40.0"))
            .envParticleLimit(1000)
            .build();

        equipmentCreateRequest = EquipmentCreateRequest.builder()
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-001")
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .equipmentDescription("New equipment")
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.75)
            .build();

        equipmentUpdateRequest = EquipmentUpdateRequest.builder()
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-002")
            .equipmentName("Updated Equipment")
            .equipmentStatus(EquipmentStatus.STOPPED)
            .equipmentGrade(EquipmentGrade.B)
            .equipmentDescription("Updated description")
            .equipmentWarrantyMonth(60)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.90)
            .build();

        existingEquipment = Equipment.builder()
            .equipmentId(4001L)
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-001")
            .equipmentName("Existing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .equipmentDescription("Existing description")
            .build();

        existingEquipmentAgingParam = EquipmentAgingParam.builder()
            .equipmentAgingParamId(5001L)
            .equipmentId(existingEquipment.getEquipmentId())
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
            .build();
    }

    @Test
    @DisplayName("Create equipment success: save equipment, aging parameters, and baseline together")
    void createEquipment_success() {
        when(equipmentQueryService.existsByEquipmentCode("EQ-001")).thenReturn(false);
        when(equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId())).thenReturn(Optional.of(equipmentProcess));
        when(environmentStandardRepository.findById(environmentStandard.getEnvironmentStandardId())).thenReturn(Optional.of(environmentStandard));
        when(idGenerator.generate()).thenReturn(4001L, 5001L, 6001L);
        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(equipmentAgingParamRepository.save(any(EquipmentAgingParam.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(equipmentBaselineRepository.save(any(EquipmentBaseline.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EquipmentCreateResponse response = equipmentCommandService.createEquipment(equipmentCreateRequest);

        ArgumentCaptor<Equipment> equipmentCaptor = ArgumentCaptor.forClass(Equipment.class);
        ArgumentCaptor<EquipmentAgingParam> agingCaptor = ArgumentCaptor.forClass(EquipmentAgingParam.class);
        ArgumentCaptor<EquipmentBaseline> baselineCaptor = ArgumentCaptor.forClass(EquipmentBaseline.class);

        verify(equipmentRepository).save(equipmentCaptor.capture());
        verify(equipmentAgingParamRepository).save(agingCaptor.capture());
        verify(equipmentBaselineRepository).save(baselineCaptor.capture());
        verify(idGenerator, times(3)).generate();

        Equipment savedEquipment = equipmentCaptor.getValue();
        EquipmentAgingParam savedAgingParam = agingCaptor.getValue();
        EquipmentBaseline savedBaseline = baselineCaptor.getValue();

        assertEquals(4001L, savedEquipment.getEquipmentId());
        assertEquals(2001L, savedEquipment.getEquipmentProcessId());
        assertEquals(3001L, savedEquipment.getEnvironmentStandardId());
        assertEquals("EQ-001", savedEquipment.getEquipmentCode());
        assertEquals("Printing Equipment", savedEquipment.getEquipmentName());
        assertEquals(EquipmentStatus.OPERATING, savedEquipment.getEquipmentStatus());
        assertEquals(EquipmentGrade.S, savedEquipment.getEquipmentGrade());
        assertEquals("New equipment", savedEquipment.getEquipmentDescription());

        assertEquals(5001L, savedAgingParam.getEquipmentAgingParamId());
        assertEquals(4001L, savedAgingParam.getEquipmentId());
        assertEquals(24, savedAgingParam.getEquipmentWarrantyMonth());
        assertEquals(120, savedAgingParam.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.75), savedAgingParam.getEquipmentWearCoefficient());

        assertEquals(6001L, savedBaseline.getEquipmentBaselineId());
        assertEquals(4001L, savedBaseline.getEquipmentId());
        assertEquals(5001L, savedBaseline.getEquipmentAgingParamId());

        assertEquals(4001L, response.getEquipmentId());
        assertEquals("EQ-001", response.getEquipmentCode());
        assertEquals("Printing Equipment", response.getEquipmentName());
        assertEquals(EquipmentStatus.OPERATING, response.getEquipmentStatus());
        assertEquals(EquipmentGrade.S, response.getEquipmentGrade());
    }

    @Test
    @DisplayName("Create equipment failure: throw exception when equipment code is duplicated")
    void createEquipment_whenDuplicateEquipmentCode_thenThrow() {
        when(equipmentQueryService.existsByEquipmentCode("EQ-001")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.createEquipment(equipmentCreateRequest));

        assertEquals(ErrorCode.EQUIPMENT_CODE_ALREADY_EXISTS, exception.getErrorCode());
        verify(equipmentRepository, never()).save(any(Equipment.class));
        verify(equipmentAgingParamRepository, never()).save(any(EquipmentAgingParam.class));
        verify(equipmentBaselineRepository, never()).save(any(EquipmentBaseline.class));
        verify(idGenerator, never()).generate();
    }

    @Test
    @DisplayName("Create equipment failure: registration is blocked when process does not exist")
    void createEquipment_whenEquipmentProcessNotFound_thenThrow() {
        when(equipmentQueryService.existsByEquipmentCode("EQ-001")).thenReturn(false);
        when(equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.createEquipment(equipmentCreateRequest));

        assertEquals(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND, exception.getErrorCode());
        verify(environmentStandardRepository, never()).findById(any());
        verify(equipmentRepository, never()).save(any(Equipment.class));
        verify(equipmentAgingParamRepository, never()).save(any(EquipmentAgingParam.class));
        verify(equipmentBaselineRepository, never()).save(any(EquipmentBaseline.class));
        verify(idGenerator, never()).generate();
    }

    @Test
    @DisplayName("Create equipment failure: registration is blocked when environment standard does not exist")
    void createEquipment_whenEnvironmentStandardNotFound_thenThrow() {
        when(equipmentQueryService.existsByEquipmentCode("EQ-001")).thenReturn(false);
        when(equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId())).thenReturn(Optional.of(equipmentProcess));
        when(environmentStandardRepository.findById(environmentStandard.getEnvironmentStandardId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.createEquipment(equipmentCreateRequest));

        assertEquals(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND, exception.getErrorCode());
        verify(equipmentRepository, never()).save(any(Equipment.class));
        verify(equipmentAgingParamRepository, never()).save(any(EquipmentAgingParam.class));
        verify(equipmentBaselineRepository, never()).save(any(EquipmentBaseline.class));
        verify(idGenerator, never()).generate();
    }

    @Test
    @DisplayName("Update equipment success: update equipment and aging parameters together")
    void updateEquipment_success() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.findByEquipmentCode("EQ-002")).thenReturn(Optional.empty());
        when(equipmentProcessRepository.findById(equipmentUpdateRequest.getEquipmentProcessId())).thenReturn(Optional.of(equipmentProcess));
        when(environmentStandardRepository.findById(equipmentUpdateRequest.getEnvironmentStandardId())).thenReturn(Optional.of(environmentStandard));
        when(equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(4001L)).thenReturn(5001L);
        when(equipmentAgingParamRepository.findById(5001L)).thenReturn(Optional.of(existingEquipmentAgingParam));

        equipmentCommandService.updateEquipment(4001L, equipmentUpdateRequest);

        assertEquals("EQ-002", existingEquipment.getEquipmentCode());
        assertEquals("Updated Equipment", existingEquipment.getEquipmentName());
        assertEquals(EquipmentStatus.STOPPED, existingEquipment.getEquipmentStatus());
        assertEquals(EquipmentGrade.B, existingEquipment.getEquipmentGrade());
        assertEquals("Updated description", existingEquipment.getEquipmentDescription());
        assertEquals(2001L, existingEquipment.getEquipmentProcessId());
        assertEquals(3001L, existingEquipment.getEnvironmentStandardId());
        assertEquals(60, existingEquipmentAgingParam.getEquipmentWarrantyMonth());
        assertEquals(120, existingEquipmentAgingParam.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.9), existingEquipmentAgingParam.getEquipmentWearCoefficient());
    }

    @Test
    @DisplayName("Update equipment failure: throw exception when equipment does not exist")
    void updateEquipment_whenEquipmentNotFound_thenThrow() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.updateEquipment(4001L, equipmentUpdateRequest));

        assertEquals(ErrorCode.EQUIPMENT_NOT_FOUND, exception.getErrorCode());
        verify(equipmentAgingParamRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Update equipment failure: throw exception when equipment code is duplicated")
    void updateEquipment_whenDuplicateEquipmentCode_thenThrow() {
        Equipment otherEquipment = Equipment.builder()
            .equipmentId(4002L)
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-002")
            .equipmentName("Other Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Other")
            .build();

        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.findByEquipmentCode("EQ-002")).thenReturn(Optional.of(otherEquipment));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.updateEquipment(4001L, equipmentUpdateRequest));

        assertEquals(ErrorCode.EQUIPMENT_CODE_ALREADY_EXISTS, exception.getErrorCode());
        verify(equipmentAgingParamRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Update equipment failure: throw exception when aging parameter id is missing")
    void updateEquipment_whenAgingParamIdIsMissing_thenThrow() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.findByEquipmentCode("EQ-002")).thenReturn(Optional.empty());
        when(equipmentProcessRepository.findById(equipmentUpdateRequest.getEquipmentProcessId())).thenReturn(Optional.of(equipmentProcess));
        when(environmentStandardRepository.findById(equipmentUpdateRequest.getEnvironmentStandardId())).thenReturn(Optional.of(environmentStandard));
        when(equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(4001L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.updateEquipment(4001L, equipmentUpdateRequest));

        assertEquals(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND, exception.getErrorCode());
        verify(equipmentAgingParamRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Delete equipment success: delete baseline, aging parameters, and equipment in order")
    void deleteEquipment_success() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(existingEquipment));
        when(equipmentQueryService.getEquipmentBaselineIdByEquipmentId(4001L)).thenReturn(6001L);
        when(equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(4001L)).thenReturn(5001L);

        equipmentCommandService.deleteEquipment(4001L);

        verify(equipmentBaselineRepository).deleteById(6001L);
        verify(equipmentAgingParamRepository).deleteById(5001L);
        verify(equipmentRepository).delete(existingEquipment);
    }

    @Test
    @DisplayName("Delete equipment failure: throw exception when equipment does not exist")
    void deleteEquipment_whenEquipmentNotFound_thenThrow() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> equipmentCommandService.deleteEquipment(4001L));

        assertEquals(ErrorCode.EQUIPMENT_NOT_FOUND, exception.getErrorCode());
        verify(equipmentBaselineRepository, never()).deleteById(any());
        verify(equipmentAgingParamRepository, never()).deleteById(any());
    }
}

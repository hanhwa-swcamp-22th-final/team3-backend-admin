package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentProcessManageCommandServiceTest {

  @Mock
  private EquipmentProcessRepository equipmentProcessRepository;

  @Mock
  private FactoryLineRepository factoryLineRepository;

  @Mock
  private IdGenerator idGenerator;

  @InjectMocks
  private EquipmentProcessManageCommandService equipmentProcessManageCommandService;

  private FactoryLine factoryLine;
  private EquipmentProcess equipmentProcess;
  private EquipmentProcessCreateRequest createRequest;
  private EquipmentProcessUpdateRequest updateRequest;

  @BeforeEach
  void setUp() {
    factoryLine = FactoryLine.builder()
        .factoryLineId(1001L)
        .factoryLineCode("LINE-001")
        .factoryLineName("Main Line")
        .build();

    equipmentProcess = EquipmentProcess.builder()
        .equipmentProcessId(2001L)
        .factoryLineId(1001L)
        .equipmentProcessCode("PROC-001")
        .equipmentProcessName("Mixing Process")
        .build();

    createRequest = EquipmentProcessCreateRequest.builder()
        .factoryLineId(1001L)
        .equipmentProcessCode("PROC-001")
        .equipmentProcessName("Mixing Process")
        .build();

    updateRequest = EquipmentProcessUpdateRequest.builder()
        .factoryLineId(1002L)
        .equipmentProcessCode("PROC-999")
        .equipmentProcessName("Fixed Process")
        .build();
  }
  @Test
  @DisplayName("Create equipment process success: save equipment process from request")
  void createEquipmentProcess_success() {
    // given
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));
    when(equipmentProcessRepository.findByEquipmentProcessCode("PROC-001"))
        .thenReturn(Optional.empty());
    when(idGenerator.generate())
        .thenReturn(2001L);
    when(equipmentProcessRepository.save(any(EquipmentProcess.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // when
    EquipmentProcessCreateResponse response = equipmentProcessManageCommandService.createEquipmentProcess(createRequest);

    // then
    ArgumentCaptor<EquipmentProcess> captor = ArgumentCaptor.forClass(EquipmentProcess.class);
    verify(equipmentProcessRepository).save(captor.capture());

    EquipmentProcess savedEquipmentProcess = captor.getValue();
    assertEquals(2001L, savedEquipmentProcess.getEquipmentProcessId());
    assertEquals(1001L, savedEquipmentProcess.getFactoryLineId());
    assertEquals("PROC-001", savedEquipmentProcess.getEquipmentProcessCode());
    assertEquals("Mixing Process", savedEquipmentProcess.getEquipmentProcessName());
    assertFalse(savedEquipmentProcess.getIsDeleted());

    assertEquals(2001L, response.getEquipmentProcessId());
    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("PROC-001", response.getEquipmentProcessCode());
    assertEquals("Mixing Process", response.getEquipmentProcessName());

  }

  @Test
  @DisplayName("Create equipment process failure: throw exception when factory line does not exist")
  void createEquipmentProcess_whenFactoryLineNotFound_thenThrow() {
    // given
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> equipmentProcessManageCommandService.createEquipmentProcess(createRequest));

    verify(equipmentProcessRepository, never()).save(any(EquipmentProcess.class));
    verify(idGenerator, never()).generate();

  }

  @Test
  @DisplayName("Create equipment process failure: throw exception when equipment process code already exists")
  void createEquipmentProcess_whenEquipmentProcessCodeAlreadyExists_thenThrow() {
    // given
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));
    when(equipmentProcessRepository.findByEquipmentProcessCode("PROC-001"))
        .thenReturn(Optional.of(equipmentProcess));

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> equipmentProcessManageCommandService.createEquipmentProcess(createRequest));

    verify(equipmentProcessRepository, never()).save(any(EquipmentProcess.class));
    verify(idGenerator, never()).generate();

  }

  @Test
  @DisplayName("Update equipment process success: update existing equipment process")
  void updateEquipmentProcess_success() {
    // given
    when(equipmentProcessRepository.findById(2001L))
        .thenReturn(Optional.of(equipmentProcess));
    when(factoryLineRepository.findById(1002L))
        .thenReturn(Optional.of(FactoryLine.builder()
            .factoryLineId(1002L)
            .factoryLineCode("LINE-002")
            .factoryLineName("Sub Line")
            .build()));

    // when
    EquipmentProcessUpdateResponse response = equipmentProcessManageCommandService.updateEquipmentProcess(2001L, updateRequest);

    // then
    assertEquals(1002L, equipmentProcess.getFactoryLineId());
    assertEquals("PROC-999", equipmentProcess.getEquipmentProcessCode());
    assertEquals("Fixed Process", equipmentProcess.getEquipmentProcessName());

    assertEquals(2001L, response.getEquipmentProcessId());
    assertEquals(1002L, response.getFactoryLineId());
    assertEquals("PROC-999", response.getEquipmentProcessCode());
    assertEquals("Fixed Process", response.getEquipmentProcessName());

  }

  @Test
  @DisplayName("Update equipment process failure: throw exception when equipment process does not exist")
  void updateEquipmentProcess_whenEquipmentProcessNotFound_thenThrow() {
    // given
    when(equipmentProcessRepository.findById(9999L))
        .thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> equipmentProcessManageCommandService.updateEquipmentProcess(9999L, updateRequest));

  }

  @Test
  @DisplayName("Delete equipment process success: soft delete existing equipment process")
  void deleteEquipmentProcess_success() {
    // given
    when(equipmentProcessRepository.findById(2001L))
        .thenReturn(Optional.of(equipmentProcess));

    // when
    EquipmentProcessUpdateResponse response = equipmentProcessManageCommandService.deleteEquipmentProcess(2001L);

    // then
    assertTrue(equipmentProcess.getIsDeleted());
    assertEquals(2001L, response.getEquipmentProcessId());
    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("PROC-001", response.getEquipmentProcessCode());
    assertEquals("Mixing Process", response.getEquipmentProcessName());

  }

  @Test
  @DisplayName("Delete equipment process failure: throw exception when equipment process does not exist")
  void deleteEquipmentProcess_whenEquipmentProcessNotFound_thenThrow() {
    // given
    when(equipmentProcessRepository.findById(9999L))
        .thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> equipmentProcessManageCommandService.deleteEquipmentProcess(9999L));

  }

}

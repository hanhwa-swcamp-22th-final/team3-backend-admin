package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
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
class FactoryLineManageCommandServiceTest {

  @Mock
  private FactoryLineRepository factoryLineRepository;

  @Mock
  private IdGenerator idGenerator;

  @InjectMocks
  private FactoryLineManageCommandService factoryLineManageCommandService;

  private FactoryLine factoryLine;
  private FactoryLineCreateRequest createRequest;
  private FactoryLineUpdateRequest updateRequest;

  @BeforeEach
  void setUp() {
    factoryLine = FactoryLine.builder()
        .factoryLineId(1001L)
        .factoryLineCode("LINE-001")
        .factoryLineName("Main Line")
        .build();

    createRequest = FactoryLineCreateRequest.builder()
        .factoryLineCode("LINE-001")
        .factoryLineName("Main Line")
        .build();

    updateRequest = FactoryLineUpdateRequest.builder()
        .factoryLineCode("LINE-999")
        .factoryLineName("Fixed Line")
        .build();
  }

  @Test
  @DisplayName("Create factory line success: save factory line from request")
  void createFactoryLine_success() {
    // given
    when(factoryLineRepository.findByFactoryLineCode("LINE-001"))
        .thenReturn(Optional.empty());
    when(idGenerator.generate())
        .thenReturn(1001L);
    when(factoryLineRepository.save(any(FactoryLine.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // when
    var response = factoryLineManageCommandService.createFactoryLine(createRequest);

    // then
    ArgumentCaptor<FactoryLine> captor = ArgumentCaptor.forClass(FactoryLine.class);
    verify(factoryLineRepository).save(captor.capture());

    FactoryLine savedFactoryLine = captor.getValue();
    assertEquals(1001L, savedFactoryLine.getFactoryLineId());
    assertEquals("LINE-001", savedFactoryLine.getFactoryLineCode());
    assertEquals("Main Line", savedFactoryLine.getFactoryLineName());
    assertFalse(savedFactoryLine.getIsDeleted());

    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("LINE-001", response.getFactoryLineCode());
    assertEquals("Main Line", response.getFactoryLineName());
  }

  @Test
  @DisplayName("Create factory line failure: throw exception when code already exists")
  void createFactoryLine_whenFactoryLineCodeAlreadyExists_thenThrow() {
    // given
    when(factoryLineRepository.findByFactoryLineCode("LINE-001"))
        .thenReturn(Optional.of(factoryLine));

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> factoryLineManageCommandService.createFactoryLine(createRequest));

    verify(factoryLineRepository, never()).save(any(FactoryLine.class));
    verify(idGenerator, never()).generate();
  }

  @Test
  @DisplayName("Update factory line success: update existing factory line")
  void updateFactoryLine_success() {
    // given
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));

    // when
    var response = factoryLineManageCommandService.updateFactoryLine(1001L, updateRequest);

    // then
    assertEquals("LINE-999", factoryLine.getFactoryLineCode());
    assertEquals("Fixed Line", factoryLine.getFactoryLineName());

    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("LINE-999", response.getFactoryLineCode());
    assertEquals("Fixed Line", response.getFactoryLineName());
  }

  @Test
  @DisplayName("Update factory line failure: throw exception when factory line does not exist")
  void updateFactoryLine_whenFactoryLineNotFound_thenThrow() {
    //given
    when(factoryLineRepository.findById(9999L))
        .thenReturn(Optional.empty());

    //when
    //then
    assertThrows(IllegalArgumentException.class,
        () -> factoryLineManageCommandService.updateFactoryLine(9999L, updateRequest));
  }

  @Test
  @DisplayName("Delete factory line success: soft delete existing factory line")
  void deleteFactoryLine_success() {
    // given
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));

    // when
    var response = factoryLineManageCommandService.deleteFactoryLine(1001L);

    // then
    assertTrue(factoryLine.getIsDeleted());
    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("LINE-001", response.getFactoryLineCode());
    assertEquals("Main Line", response.getFactoryLineName());
  }

  @Test
  @DisplayName("Delete factory line failure: throw exception when factory line does not exist")
  void deleteFactoryLine_whenFactoryLineNotFound_thenThrow() {
    // given
    when(factoryLineRepository.findById(9999L))
        .thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> factoryLineManageCommandService.deleteFactoryLine(9999L));
  }

}

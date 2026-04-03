package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    when(factoryLineRepository.findByFactoryLineCode("LINE-001"))
        .thenReturn(Optional.empty());
    when(idGenerator.generate())
        .thenReturn(1001L);
    when(factoryLineRepository.save(any(FactoryLine.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    FactoryLineCreateResponse response = factoryLineManageCommandService.createFactoryLine(createRequest);

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
  @DisplayName("Create factory line failure: throw business exception when code already exists")
  void createFactoryLine_whenFactoryLineCodeAlreadyExists_thenThrow() {
    when(factoryLineRepository.findByFactoryLineCode("LINE-001"))
        .thenReturn(Optional.of(factoryLine));

    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> factoryLineManageCommandService.createFactoryLine(createRequest)
    );

    assertEquals(ErrorCode.FACTORY_LINE_CODE_ALREADY_EXISTS, exception.getErrorCode());
    verify(factoryLineRepository, never()).save(any(FactoryLine.class));
    verify(idGenerator, never()).generate();
  }

  @Test
  @DisplayName("Update factory line success: update existing factory line")
  void updateFactoryLine_success() {
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));
    when(factoryLineRepository.findByFactoryLineCode("LINE-999"))
        .thenReturn(Optional.empty());

    FactoryLineUpdateResponse response = factoryLineManageCommandService.updateFactoryLine(1001L, updateRequest);

    assertEquals("LINE-999", factoryLine.getFactoryLineCode());
    assertEquals("Fixed Line", factoryLine.getFactoryLineName());

    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("LINE-999", response.getFactoryLineCode());
    assertEquals("Fixed Line", response.getFactoryLineName());
  }

  @Test
  @DisplayName("Update factory line failure: throw business exception when factory line does not exist")
  void updateFactoryLine_whenFactoryLineNotFound_thenThrow() {
    when(factoryLineRepository.findById(9999L))
        .thenReturn(Optional.empty());

    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> factoryLineManageCommandService.updateFactoryLine(9999L, updateRequest)
    );

    assertEquals(ErrorCode.FACTORY_LINE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @DisplayName("Update factory line failure: throw business exception when code already exists on another line")
  void updateFactoryLine_whenCodeAlreadyExists_thenThrow() {
    FactoryLine duplicatedFactoryLine = FactoryLine.builder()
        .factoryLineId(2001L)
        .factoryLineCode("LINE-999")
        .factoryLineName("Duplicated Line")
        .build();

    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));
    when(factoryLineRepository.findByFactoryLineCode("LINE-999"))
        .thenReturn(Optional.of(duplicatedFactoryLine));

    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> factoryLineManageCommandService.updateFactoryLine(1001L, updateRequest)
    );

    assertEquals(ErrorCode.FACTORY_LINE_CODE_ALREADY_EXISTS, exception.getErrorCode());
  }

  @Test
  @DisplayName("Delete factory line success: soft delete existing factory line")
  void deleteFactoryLine_success() {
    when(factoryLineRepository.findById(1001L))
        .thenReturn(Optional.of(factoryLine));

    FactoryLineUpdateResponse response = factoryLineManageCommandService.deleteFactoryLine(1001L);

    assertTrue(factoryLine.getIsDeleted());
    assertEquals(1001L, response.getFactoryLineId());
    assertEquals("LINE-001", response.getFactoryLineCode());
    assertEquals("Main Line", response.getFactoryLineName());
  }

  @Test
  @DisplayName("Delete factory line failure: throw business exception when factory line does not exist")
  void deleteFactoryLine_whenFactoryLineNotFound_thenThrow() {
    when(factoryLineRepository.findById(9999L))
        .thenReturn(Optional.empty());

    BusinessException exception = assertThrows(
        BusinessException.class,
        () -> factoryLineManageCommandService.deleteFactoryLine(9999L)
    );

    assertEquals(ErrorCode.FACTORY_LINE_NOT_FOUND, exception.getErrorCode());
  }
}

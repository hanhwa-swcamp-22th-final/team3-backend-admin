//package com.ohgiraffers.team3backendadmin.admin.command.application.service;
//
//import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.*;
//import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
//import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
//import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
//import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class EquipmentCommandServiceTest {
//
//  @Mock
//  private EquipmentRepository equipmentRepository;
//
//  @Mock
//  private EquipmentProcessRepository equipmentProcessRepository;
//
//  @Mock
//  private EnvironmentStandardRepository environmentStandardRepository;
//
//  @Mock
//  private IdGenerator idGenerator;
//
//  @InjectMocks
//  private EquipmentCommandService equipmentCommandService;
//
//  private FactoryLine factoryLine;
//  private EquipmentProcess equipmentProcess;
//  private EnvironmentStandard environmentStandard;
//
//  @BeforeEach
//  void setUp() {
//    factoryLine = new FactoryLine(
//        1001L,
//        "LINE-01",
//        "1라인"
//    );
//
//    equipmentProcess = new EquipmentProcess(
//        2001L,
//        factoryLine.getFactoryLineId(),
//        "PROC-01",
//        "건조 공정"
//    );
//
//    environmentStandard = new EnvironmentStandard(
//        3001L,
//        EnvironmentType.DRYROOM,
//        new BigDecimal("20.0"),
//        new BigDecimal("25.0"),
//        new BigDecimal("30.0"),
//        new BigDecimal("40.0"),
//        1000
//    );
//  }
//
//  @Test
//  @DisplayName("설비 등록 성공: 요청값으로 설비를 등록한다")
//  void createEquipment_success() {
//    // given
//    EquipmentCreateEquipmentRequest request = new EquipmentCreateEquipmentRequest(
//        equipmentProcess.getEquipmentProcessId(),
//        environmentStandard.getEnvironmentStandardId(),
//        "EQ-001",
//        "건조 설비",
//        EquipmentStatus.OPERATING,
//        EquipmentGrade.A,
//        null,
//        "초기 설명",
//        24,
//        120,
//        new BigDecimal("0.75")
//    );
//
//    when(equipmentRepository.findByEquipmentCode("EQ-001"))
//        .thenReturn(Optional.empty());
//    when(equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId()))
//        .thenReturn(Optional.of(equipmentProcess));
//    when(environmentStandardRepository.findById(environmentStandard.getEnvironmentStandardId()))
//        .thenReturn(Optional.of(environmentStandard));
//    when(idGenerator.generate())
//        .thenReturn(4001L);
//    when(equipmentRepository.save(any(Equipment.class)))
//        .thenAnswer(invocation -> invocation.getArgument(0));
//
//    // when
//    equipmentCommandService.createEquipment(request);
//
//    // then
//    ArgumentCaptor<Equipment> captor = ArgumentCaptor.forClass(Equipment.class);
//    verify(equipmentRepository).save(captor.capture());
//
//    Equipment savedEquipment = captor.getValue();
//
//    assertEquals(4001L, savedEquipment.getEquipmentId());
//    assertEquals(equipmentProcess.getEquipmentProcessId(), savedEquipment.getEquipmentProcessId());
//    assertEquals(environmentStandard.getEnvironmentStandardId(), savedEquipment.getEnvironmentStandardId());
//    assertEquals("EQ-001", savedEquipment.getEquipmentCode());
//    assertEquals("건조 설비", savedEquipment.getEquipmentName());
//    assertEquals(EquipmentStatus.OPERATING, savedEquipment.getEquipmentStatus());
//    assertEquals(EquipmentGrade.A, savedEquipment.getEquipmentGrade());
//    assertEquals("초기 설명", savedEquipment.getEquipmentDescription());
//
//    verify(equipmentRepository).findByEquipmentCode("EQ-001");
//    verify(equipmentProcessRepository).findById(equipmentProcess.getEquipmentProcessId());
//    verify(environmentStandardRepository).findById(environmentStandard.getEnvironmentStandardId());
//    verify(idGenerator).generate();
//  }
//
//  @Test
//  @DisplayName("설비 등록 실패: 중복 설비 코드면 예외가 발생한다")
//  void createEquipment_whenDuplicateEquipmentCode_thenThrow() {
//  }
//
//  @Test
//  @DisplayName("설비 등록 실패: 설비 코드가 비어 있으면 예외가 발생한다")
//  void createEquipment_whenRequestHasBlankEquipmentCode_thenThrow() {
//  }
//
//  @Test
//  @DisplayName("설비 수정 성공: 기존 설비 정보를 수정한다")
//  void updateEquipment_success() {
//  }
//
//  @Test
//  @DisplayName("설비 수정 실패: 설비가 없으면 예외가 발생한다")
//  void updateEquipment_whenEquipmentNotFound_thenThrow() {
//  }
//
//  @Test
//  @DisplayName("설비 삭제 성공: 기존 설비를 삭제한다")
//  void deleteEquipment_success() {
//  }
//
//  @Test
//  @DisplayName("설비 삭제 실패: 설비가 없으면 예외가 발생한다")
//  void deleteEquipment_whenEquipmentNotFound_thenThrow() {
//  }
//}

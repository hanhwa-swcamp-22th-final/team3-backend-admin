package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.*;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentRepositoryTest {

  @Autowired
  private EquipmentRepository equipmentRepository;

  @Autowired
  private FactoryLineRepository factoryLineRepository;

  @Autowired
  private EquipmentProcessRepository equipmentProcessRepository;

  @Autowired
  private EnvironmentStandardRepository environmentStandardRepository;

  private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

  private FactoryLine factoryLine;
  private EquipmentProcess equipmentProcess;
  private EnvironmentStandard environmentStandard;

  @BeforeEach
  void setUp() {
    factoryLine = factoryLineRepository.save(
        new FactoryLine(
            idGenerator.generate(),
            "LINE-" + idGenerator.generate(),
            "1라인"
        )
    );

    equipmentProcess = equipmentProcessRepository.save(
        new EquipmentProcess(
            idGenerator.generate(),
            factoryLine.getFactoryLineId(),
            "PROC-" + idGenerator.generate(),
            "건조 공정"
        )
    );

    environmentStandard = environmentStandardRepository.save(
        new EnvironmentStandard(
            idGenerator.generate(),
            EnvironmentType.DRYROOM,
            BigDecimal.valueOf(20.0),
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            BigDecimal.valueOf(40.0),
            1000
        )
    );
  }

  @Test
  @DisplayName("설비 저장 성공: 엔티티가 저장된다")
  void save_success() {
    Equipment equipment = new Equipment(
        idGenerator.generate(),
        equipmentProcess.getEquipmentProcessId(),
        environmentStandard.getEnvironmentStandardId(),
        "EQ-" + idGenerator.generate(),
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명"
    );

    Equipment savedEquipment = equipmentRepository.save(equipment);

    assertNotNull(savedEquipment);
    assertEquals(equipment.getEquipmentId(), savedEquipment.getEquipmentId());
    assertEquals(equipment.getEquipmentCode(), savedEquipment.getEquipmentCode());
  }

  @Test
  @DisplayName("설비 ID 조회 성공: 저장한 설비를 조회한다")
  void findById_success() {
    Equipment equipment = equipmentRepository.save(
        new Equipment(
            idGenerator.generate(),
            equipmentProcess.getEquipmentProcessId(),
            environmentStandard.getEnvironmentStandardId(),
            "EQ-" + idGenerator.generate(),
            "혼합 설비",
            EquipmentStatus.OPERATING,
            EquipmentGrade.B,
            "상세 설명"
        )
    );

    Optional<Equipment> result = equipmentRepository.findById(equipment.getEquipmentId());

    assertTrue(result.isPresent());
    assertEquals(equipment.getEquipmentId(), result.get().getEquipmentId());
    assertEquals("혼합 설비", result.get().getEquipmentName());
  }

  @Test
  @DisplayName("설비 코드 조회 성공: 등록된 코드로 설비를 조회한다")
  void findByEquipmentCode_success() {
    String equipmentCode = "EQ-" + idGenerator.generate();

    equipmentRepository.save(
        new Equipment(
            idGenerator.generate(),
            equipmentProcess.getEquipmentProcessId(),
            environmentStandard.getEnvironmentStandardId(),
            equipmentCode,
            "코팅 설비",
            EquipmentStatus.OPERATING,
            EquipmentGrade.S,
            "코드 조회 테스트"
        )
    );

    Optional<Equipment> result = equipmentRepository.findByEquipmentCode(equipmentCode);

    assertTrue(result.isPresent());
    assertEquals(equipmentCode, result.get().getEquipmentCode());
    assertEquals("코팅 설비", result.get().getEquipmentName());
  }

  @Test
  @DisplayName("설비 코드 조회 실패: 존재하지 않는 코드면 empty를 반환한다")
  void findByEquipmentCode_whenNotFound_thenEmpty() {
    Optional<Equipment> result = equipmentRepository.findByEquipmentCode("EQ-NOT-FOUND");

    assertTrue(result.isEmpty());
  }
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentProcessRepositoryTest {

  @Autowired
  private FactoryLineRepository factoryLineRepository;

  @Autowired
  private EquipmentProcessRepository equipmentProcessRepository;

  private FactoryLine factoryLine;
  private EquipmentProcess equipmentProcess;

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
  }

  @Test
  @DisplayName("Save equipment process success: equipment process is persisted")
  void save_success() {
    factoryLineRepository.save(factoryLine);

    EquipmentProcess savedEquipmentProcess = equipmentProcessRepository.save(equipmentProcess);

    assertNotNull(savedEquipmentProcess);
    assertEquals(2001L, savedEquipmentProcess.getEquipmentProcessId());
    assertEquals(1001L, savedEquipmentProcess.getFactoryLineId());
    assertEquals("PROC-001", savedEquipmentProcess.getEquipmentProcessCode());
    assertEquals("Mixing Process", savedEquipmentProcess.getEquipmentProcessName());
    assertFalse(savedEquipmentProcess.getIsDeleted());
  }

  @Test
  @DisplayName("Find equipment process by id success: return persisted equipment process")
  void findById_success() {
    factoryLineRepository.save(factoryLine);
    equipmentProcessRepository.save(equipmentProcess);

    Optional<EquipmentProcess> result = equipmentProcessRepository.findById(2001L);

    assertTrue(result.isPresent());
    assertEquals("PROC-001", result.get().getEquipmentProcessCode());
  }

  @Test
  @DisplayName("Find equipment process by id failure: return empty when equipment process does not exist")
  void findById_whenNotFound_thenEmpty() {
    Optional<EquipmentProcess> result = equipmentProcessRepository.findById(9999L);

    assertFalse(result.isPresent());
  }
}

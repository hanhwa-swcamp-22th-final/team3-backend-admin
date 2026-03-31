package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

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
class FactoryLineRepositoryTest {

  @Autowired
  private FactoryLineRepository factoryLineRepository;

  private FactoryLine factoryLine;

  @BeforeEach
  void setUp() {
    factoryLine = FactoryLine.builder()
        .factoryLineId(1001L)
        .factoryLineCode("LINE-001")
        .factoryLineName("Main Line")
        .build();
  }
  @Test
  @DisplayName("Save factory line success: factory line is persisted")
  void save_success() {
    FactoryLine savedFactoryLine = factoryLineRepository.save(factoryLine);

    assertNotNull(savedFactoryLine);
    assertEquals(1001L, savedFactoryLine.getFactoryLineId());
    assertEquals("LINE-001", savedFactoryLine.getFactoryLineCode());
    assertEquals("Main Line", savedFactoryLine.getFactoryLineName());
    assertFalse(savedFactoryLine.getIsDeleted());
  }

  @Test
  @DisplayName("Find factory line by id success: return persisted factory line")
  void findById_success() {
    factoryLineRepository.save(factoryLine);

    Optional<FactoryLine> result = factoryLineRepository.findById(1001L);

    assertTrue(result.isPresent());
    assertEquals("LINE-001", result.get().getFactoryLineCode());
  }

  @Test
  @DisplayName("Find factory line by id failure: return empty when factory line does not exist")
  void findById_whenNotFound_thenEmpty() {
    Optional<FactoryLine> result = factoryLineRepository.findById(9999L);

    assertFalse(result.isPresent());
  }

}
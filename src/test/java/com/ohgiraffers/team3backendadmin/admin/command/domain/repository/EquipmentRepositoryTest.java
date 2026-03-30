package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Line 1"
            )
        );

        equipmentProcess = equipmentProcessRepository.save(
            new EquipmentProcess(
                idGenerator.generate(),
                factoryLine.getFactoryLineId(),
                "PROC-" + idGenerator.generate(),
                "Drying Process"
            )
        );

        environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(idGenerator.generate())
                .environmentType(EnvironmentType.DRYROOM)
                .enviromentCode("ENV-" + idGenerator.generate())
                .enviromentName("Dry Room Standard")
                .envTempMin(BigDecimal.valueOf(20.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        equipment = Equipment.builder()
            .equipmentId(idGenerator.generate())
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-" + idGenerator.generate())
            .equipmentName("Drying Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Initial description")
            .build();
    }

    @Test
    @DisplayName("Save equipment success: entity is stored")
    void save_success() {
        Equipment savedEquipment = equipmentRepository.save(equipment);

        assertNotNull(savedEquipment);
        assertEquals(equipment.getEquipmentId(), savedEquipment.getEquipmentId());
        assertEquals(equipment.getEquipmentCode(), savedEquipment.getEquipmentCode());
        assertEquals("Drying Equipment", savedEquipment.getEquipmentName());
    }

    @Test
    @DisplayName("Find equipment by id success: stored equipment is returned")
    void findById_success() {
        Equipment savedEquipment = equipmentRepository.save(equipment);

        Optional<Equipment> result = equipmentRepository.findById(savedEquipment.getEquipmentId());

        assertTrue(result.isPresent());
        assertEquals(savedEquipment.getEquipmentId(), result.get().getEquipmentId());
        assertEquals("Drying Equipment", result.get().getEquipmentName());
        assertEquals(EquipmentGrade.A, result.get().getEquipmentGrade());
    }

    @Test
    @DisplayName("Find equipment by id failure: return empty when id does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<Equipment> result = equipmentRepository.findById(-1L);

        assertTrue(result.isEmpty());
    }
}
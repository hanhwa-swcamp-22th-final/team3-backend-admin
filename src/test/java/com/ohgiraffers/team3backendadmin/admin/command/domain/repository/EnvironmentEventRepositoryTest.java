package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnvironmentEventRepositoryTest {

    @Autowired
    private EnvironmentEventRepository environmentEventRepository;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private EnvironmentEvent environmentEvent;
    private Long environmentEventId;
    private Long equipmentId;

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(idGenerator.generate());
        Long factoryLineId = idGenerator.generate();
        Long equipmentProcessId = idGenerator.generate();
        Long environmentStandardId = idGenerator.generate();
        equipmentId = idGenerator.generate();
        environmentEventId = idGenerator.generate();

        FactoryLine factoryLine = factoryLineRepository.save(
            FactoryLine.builder()
                .factoryLineId(factoryLineId)
                .factoryLineCode("LINE-ENV-" + uniqueSuffix)
                .factoryLineName("Environment Line " + uniqueSuffix)
                .build()
        );

        EquipmentProcess equipmentProcess = equipmentProcessRepository.save(
            EquipmentProcess.builder()
                .equipmentProcessId(equipmentProcessId)
                .factoryLineId(factoryLine.getFactoryLineId())
                .equipmentProcessCode("PROC-ENV-" + uniqueSuffix)
                .equipmentProcessName("Environment Process " + uniqueSuffix)
                .build()
        );

        EnvironmentStandard environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(environmentStandardId)
                .environmentType(EnvironmentType.DRYROOM)
                .environmentCode("ENV-STD-" + uniqueSuffix)
                .environmentName("Dry Room " + uniqueSuffix)
                .envTempMin(BigDecimal.valueOf(18.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        equipmentRepository.save(
            Equipment.builder()
                .equipmentId(equipmentId)
                .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
                .environmentStandardId(environmentStandard.getEnvironmentStandardId())
                .equipmentCode("EQ-ENV-" + uniqueSuffix)
                .equipmentName("Environment Equipment " + uniqueSuffix)
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.S)
                .equipmentDescription("Repository test equipment")
                .build()
        );

        environmentEvent = EnvironmentEvent.builder()
            .environmentEventId(environmentEventId)
            .equipmentId(equipmentId)
            .envTemperature(BigDecimal.valueOf(24.0))
            .envHumidity(BigDecimal.valueOf(38.0))
            .envParticleCnt(90)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
            .build();
    }

    @Test
    @DisplayName("Save environment event success: environment event is persisted")
    void save_success() {
        EnvironmentEvent savedEnvironmentEvent = environmentEventRepository.save(environmentEvent);

        assertNotNull(savedEnvironmentEvent);
        assertEquals(environmentEventId, savedEnvironmentEvent.getEnvironmentEventId());
        assertEquals(equipmentId, savedEnvironmentEvent.getEquipmentId());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, savedEnvironmentEvent.getEnvDeviationType());
    }

    @Test
    @DisplayName("Find environment event by id success: return persisted environment event")
    void findById_success() {
        environmentEventRepository.save(environmentEvent);

        Optional<EnvironmentEvent> result = environmentEventRepository.findById(environmentEventId);

        assertTrue(result.isPresent());
        assertEquals(equipmentId, result.get().getEquipmentId());
    }

    @Test
    @DisplayName("Find environment event by id failure: return empty when environment event does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<EnvironmentEvent> result = environmentEventRepository.findById(idGenerator.generate());

        assertFalse(result.isPresent());
    }
}
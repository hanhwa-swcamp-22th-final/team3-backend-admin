package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentBaselineRepositoryTest {

    @Autowired
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Autowired
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private Equipment equipment;
    private EquipmentAgingParam equipmentAgingParam;
    private EquipmentBaseline equipmentBaseline;

    @BeforeEach
    void setUp() {
        FactoryLine factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Line 1"
            )
        );

        EquipmentProcess equipmentProcess = equipmentProcessRepository.save(
            new EquipmentProcess(
                idGenerator.generate(),
                factoryLine.getFactoryLineId(),
                "PROC-" + idGenerator.generate(),
                "Drying Process"
            )
        );

        EnvironmentStandard environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(idGenerator.generate())
                .environmentType(EnvironmentType.DRYROOM)
                .environmentCode("ENV-" + idGenerator.generate())
                .environmentName("Dry Room Standard")
                .envTempMin(BigDecimal.valueOf(20.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        equipment = equipmentRepository.save(
            Equipment.builder()
                .equipmentId(idGenerator.generate())
                .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
                .environmentStandardId(environmentStandard.getEnvironmentStandardId())
                .equipmentCode("EQ-" + idGenerator.generate())
                .equipmentName("Baseline Test Equipment")
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.A)
                .equipmentDescription("Baseline repository test")
                .build()
        );

        equipmentAgingParam = equipmentAgingParamRepository.save(
            EquipmentAgingParam.builder()
                .equipmentAgingParamId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .equipmentWarrantyMonth(24)
                .equipmentDesignLifeMonths(120)
                .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
                .build()
        );

        equipmentBaseline = EquipmentBaseline.builder()
            .equipmentBaselineId(idGenerator.generate())
            .equipmentId(equipment.getEquipmentId())
            .equipmentAgingParamId(equipmentAgingParam.getEquipmentAgingParamId())
            .equipmentStandardPerformanceRate(BigDecimal.valueOf(95.0))
            .equipmentBaselineErrorRate(BigDecimal.valueOf(2.5))
            .equipmentEtaMaint(BigDecimal.valueOf(12.0))
            .equipmentIdx(BigDecimal.valueOf(88.0))
            .equipmentBaselineCalculatedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
            .build();
    }

    @Test
    @DisplayName("Save equipment baseline success: entity is stored")
    void save_success() {
        EquipmentBaseline savedEquipmentBaseline = equipmentBaselineRepository.save(equipmentBaseline);

        assertNotNull(savedEquipmentBaseline);
        assertEquals(equipmentBaseline.getEquipmentBaselineId(), savedEquipmentBaseline.getEquipmentBaselineId());
        assertEquals(equipment.getEquipmentId(), savedEquipmentBaseline.getEquipmentId());
        assertEquals(equipmentAgingParam.getEquipmentAgingParamId(), savedEquipmentBaseline.getEquipmentAgingParamId());
    }

    @Test
    @DisplayName("Find equipment baseline by id success: stored entity is returned")
    void findById_success() {
        EquipmentBaseline savedEquipmentBaseline = equipmentBaselineRepository.save(equipmentBaseline);

        Optional<EquipmentBaseline> result = equipmentBaselineRepository.findById(savedEquipmentBaseline.getEquipmentBaselineId());

        assertTrue(result.isPresent());
        assertEquals(savedEquipmentBaseline.getEquipmentBaselineId(), result.get().getEquipmentBaselineId());
        assertEquals(BigDecimal.valueOf(95.0), result.get().getEquipmentStandardPerformanceRate());
        assertEquals(BigDecimal.valueOf(88.0), result.get().getEquipmentIdx());
    }

    @Test
    @DisplayName("Find equipment baseline by id failure: return empty when id does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<EquipmentBaseline> result = equipmentBaselineRepository.findById(-1L);

        assertTrue(result.isEmpty());
    }
}

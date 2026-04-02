package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentAgingParamRepositoryTest {

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
                .equipmentName("Aging Test Equipment")
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.A)
                .equipmentDescription("Aging parameter repository test")
                .build()
        );

        equipmentAgingParam = EquipmentAgingParam.builder()
            .equipmentAgingParamId(idGenerator.generate())
            .equipmentId(equipment.getEquipmentId())
            .equipmentEtaAge(BigDecimal.valueOf(18.5))
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
            .equipmentAgeMonths(36)
            .build();
    }

    @Test
    @DisplayName("Save equipment aging param success: entity is stored")
    void save_success() {
        EquipmentAgingParam savedEquipmentAgingParam = equipmentAgingParamRepository.save(equipmentAgingParam);

        assertNotNull(savedEquipmentAgingParam);
        assertEquals(equipmentAgingParam.getEquipmentAgingParamId(), savedEquipmentAgingParam.getEquipmentAgingParamId());
        assertEquals(equipment.getEquipmentId(), savedEquipmentAgingParam.getEquipmentId());
        assertEquals(BigDecimal.valueOf(18.5), savedEquipmentAgingParam.getEquipmentEtaAge());
    }

    @Test
    @DisplayName("Find equipment aging param by id success: stored entity is returned")
    void findById_success() {
        EquipmentAgingParam savedEquipmentAgingParam = equipmentAgingParamRepository.save(equipmentAgingParam);

        Optional<EquipmentAgingParam> result = equipmentAgingParamRepository.findById(savedEquipmentAgingParam.getEquipmentAgingParamId());

        assertTrue(result.isPresent());
        assertEquals(savedEquipmentAgingParam.getEquipmentAgingParamId(), result.get().getEquipmentAgingParamId());
        assertEquals(24, result.get().getEquipmentWarrantyMonth());
        assertEquals(120, result.get().getEquipmentDesignLifeMonths());
    }

    @Test
    @DisplayName("Find equipment aging param by id failure: return empty when id does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<EquipmentAgingParam> result = equipmentAgingParamRepository.findById(-1L);

        assertTrue(result.isEmpty());
    }
}

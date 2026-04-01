package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceLog;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MaintenanceLogRepositoryTest {

    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

    @Autowired
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private MaintenanceLog maintenanceLog;
    private Long maintenanceLogId;
    private Long equipmentId;

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(idGenerator.generate());
        Long factoryLineId = idGenerator.generate();
        Long equipmentProcessId = idGenerator.generate();
        Long environmentStandardId = idGenerator.generate();
        Long maintenanceItemStandardId = idGenerator.generate();
        equipmentId = idGenerator.generate();
        maintenanceLogId = idGenerator.generate();

        FactoryLine factoryLine = factoryLineRepository.save(
            FactoryLine.builder()
                .factoryLineId(factoryLineId)
                .factoryLineCode("LINE-MAIN-" + uniqueSuffix)
                .factoryLineName("Maintenance Line " + uniqueSuffix)
                .build()
        );

        EquipmentProcess equipmentProcess = equipmentProcessRepository.save(
            EquipmentProcess.builder()
                .equipmentProcessId(equipmentProcessId)
                .factoryLineId(factoryLine.getFactoryLineId())
                .equipmentProcessCode("PROC-MAIN-" + uniqueSuffix)
                .equipmentProcessName("Maintenance Process " + uniqueSuffix)
                .build()
        );

        EnvironmentStandard environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(environmentStandardId)
                .environmentType(EnvironmentType.DRYROOM)
                .environmentCode("ENV-MAIN-" + uniqueSuffix)
                .environmentName("Maintenance Environment " + uniqueSuffix)
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
                .equipmentCode("EQ-MAIN-" + uniqueSuffix)
                .equipmentName("Maintenance Equipment " + uniqueSuffix)
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.S)
                .equipmentDescription("Repository test equipment")
                .build()
        );

        MaintenanceItemStandard maintenanceItemStandard = maintenanceItemStandardRepository.save(
            MaintenanceItemStandard.builder()
                .maintenanceItemStandardId(maintenanceItemStandardId)
                .maintenanceItem("Bearing Inspection " + uniqueSuffix)
                .maintenanceWeight(BigDecimal.valueOf(1.5))
                .maintenanceScoreMax(BigDecimal.valueOf(10.0))
                .build()
        );

        maintenanceLog = MaintenanceLog.builder()
            .maintenanceLogId(maintenanceLogId)
            .equipmentId(equipmentId)
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceDate(LocalDate.of(2026, 3, 31))
            .maintenanceScore(BigDecimal.valueOf(9.0))
            .etaMaintDelta(BigDecimal.valueOf(1.2))
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();
    }

    @Test
    @DisplayName("Save maintenance log success: maintenance log is persisted")
    void save_success() {
        MaintenanceLog savedMaintenanceLog = maintenanceLogRepository.save(maintenanceLog);

        assertNotNull(savedMaintenanceLog);
        assertEquals(maintenanceLogId, savedMaintenanceLog.getMaintenanceLogId());
        assertEquals(equipmentId, savedMaintenanceLog.getEquipmentId());
        assertEquals(MaintenanceType.REGULAR, savedMaintenanceLog.getMaintenanceType());
    }

    @Test
    @DisplayName("Find maintenance log by id success: return persisted maintenance log")
    void findById_success() {
        maintenanceLogRepository.save(maintenanceLog);

        Optional<MaintenanceLog> result = maintenanceLogRepository.findById(maintenanceLogId);

        assertTrue(result.isPresent());
        assertEquals(equipmentId, result.get().getEquipmentId());
    }

    @Test
    @DisplayName("Find maintenance log by id failure: return empty when maintenance log does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<MaintenanceLog> result = maintenanceLogRepository.findById(idGenerator.generate());

        assertFalse(result.isPresent());
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MaintenanceItemStandardRepositoryTest {

    @Autowired
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private MaintenanceItemStandard maintenanceItemStandard;
    private Long maintenanceItemStandardId;
    private String maintenanceItem;

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(idGenerator.generate());
        maintenanceItemStandardId = idGenerator.generate();
        maintenanceItem = "Bearing Inspection " + uniqueSuffix;

        maintenanceItemStandard = MaintenanceItemStandard.builder()
            .maintenanceItemStandardId(maintenanceItemStandardId)
            .maintenanceItem(maintenanceItem)
            .maintenanceWeight(BigDecimal.valueOf(1.5))
            .maintenanceScoreMax(BigDecimal.valueOf(10.0))
            .build();
    }

    @Test
    @DisplayName("Save maintenance item standard success: maintenance item standard is persisted")
    void save_success() {
        MaintenanceItemStandard savedMaintenanceItemStandard = maintenanceItemStandardRepository.save(maintenanceItemStandard);

        assertNotNull(savedMaintenanceItemStandard);
        assertEquals(maintenanceItemStandardId, savedMaintenanceItemStandard.getMaintenanceItemStandardId());
        assertEquals(maintenanceItem, savedMaintenanceItemStandard.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(1.5), savedMaintenanceItemStandard.getMaintenanceWeight());
    }

    @Test
    @DisplayName("Find maintenance item standard by id success: return persisted maintenance item standard")
    void findById_success() {
        maintenanceItemStandardRepository.save(maintenanceItemStandard);

        Optional<MaintenanceItemStandard> result = maintenanceItemStandardRepository.findById(maintenanceItemStandardId);

        assertTrue(result.isPresent());
        assertEquals(maintenanceItem, result.get().getMaintenanceItem());
    }

    @Test
    @DisplayName("Find maintenance item standard by id failure: return empty when maintenance item standard does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<MaintenanceItemStandard> result = maintenanceItemStandardRepository.findById(idGenerator.generate());

        assertFalse(result.isPresent());
    }
}

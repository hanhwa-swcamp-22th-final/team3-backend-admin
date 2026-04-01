package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
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
class EnvironmentStandardRepositoryTest {

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private EnvironmentStandard environmentStandard;
    private Long environmentStandardId;
    private String environmentCode;

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(idGenerator.generate());
        environmentStandardId = idGenerator.generate();
        environmentCode = "ENV-" + uniqueSuffix;

        environmentStandard = EnvironmentStandard.builder()
            .environmentStandardId(environmentStandardId)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode(environmentCode)
            .environmentName("Dry Room " + uniqueSuffix)
            .envTempMin(BigDecimal.valueOf(18.0))
            .envTempMax(BigDecimal.valueOf(25.0))
            .envHumidityMin(BigDecimal.valueOf(30.0))
            .envHumidityMax(BigDecimal.valueOf(40.0))
            .envParticleLimit(100)
            .build();
    }

    @Test
    @DisplayName("Save environment standard success: environment standard is persisted")
    void save_success() {
        EnvironmentStandard savedEnvironmentStandard = environmentStandardRepository.save(environmentStandard);

        assertNotNull(savedEnvironmentStandard);
        assertEquals(environmentStandardId, savedEnvironmentStandard.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, savedEnvironmentStandard.getEnvironmentType());
        assertEquals(environmentCode, savedEnvironmentStandard.getEnvironmentCode());
        assertFalse(savedEnvironmentStandard.getIsDeleted());
    }

    @Test
    @DisplayName("Find environment standard by id success: return persisted environment standard")
    void findById_success() {
        environmentStandardRepository.save(environmentStandard);

        Optional<EnvironmentStandard> result = environmentStandardRepository.findById(environmentStandardId);

        assertTrue(result.isPresent());
        assertEquals(environmentCode, result.get().getEnvironmentCode());
    }

    @Test
    @DisplayName("Find environment standard by id failure: return empty when environment standard does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<EnvironmentStandard> result = environmentStandardRepository.findById(idGenerator.generate());

        assertFalse(result.isPresent());
    }
}
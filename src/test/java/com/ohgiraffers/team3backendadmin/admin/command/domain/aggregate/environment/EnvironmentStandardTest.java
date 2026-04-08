package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentStandardTest {

    private EnvironmentStandard environmentStandard;

    @BeforeEach
    void setUp() {
        environmentStandard = EnvironmentStandard.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .envTempMin(BigDecimal.valueOf(18.0))
            .envTempMax(BigDecimal.valueOf(25.0))
            .envHumidityMin(BigDecimal.valueOf(30.0))
            .envHumidityMax(BigDecimal.valueOf(40.0))
            .envParticleLimit(100)
            .build();
    }

    @Test
    @DisplayName("Update environment standard success: fields are updated correctly")
    void updateInfo_success() {
        environmentStandard.updateInfo(
            EnvironmentType.CLEANROOM,
            "ENV-999",
            "Clean Room",
            BigDecimal.valueOf(19.0),
            BigDecimal.valueOf(26.0),
            BigDecimal.valueOf(35.0),
            BigDecimal.valueOf(45.0),
            200
        );

        assertEquals(EnvironmentType.CLEANROOM, environmentStandard.getEnvironmentType());
        assertEquals("ENV-999", environmentStandard.getEnvironmentCode());
        assertEquals("Clean Room", environmentStandard.getEnvironmentName());
        assertEquals(BigDecimal.valueOf(19.0), environmentStandard.getEnvTempMin());
        assertEquals(BigDecimal.valueOf(26.0), environmentStandard.getEnvTempMax());
        assertEquals(BigDecimal.valueOf(35.0), environmentStandard.getEnvHumidityMin());
        assertEquals(BigDecimal.valueOf(45.0), environmentStandard.getEnvHumidityMax());
        assertEquals(200, environmentStandard.getEnvParticleLimit());
    }

    @Test
    @DisplayName("Soft delete environment standard success: isDeleted is changed to true")
    void softDelete_success() {
        environmentStandard.softDelete();

        assertTrue(environmentStandard.getIsDeleted());
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when environment type is null")
    void updateInfo_whenEnvironmentTypeIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                null,
                "ENV-999",
                "Clean Room",
                BigDecimal.valueOf(19.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(35.0),
                BigDecimal.valueOf(45.0),
                200
            )
        );
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when environment code is blank")
    void updateInfo_whenEnvironmentCodeIsBlank_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                EnvironmentType.DRYROOM,
                " ",
                "Clean Room",
                BigDecimal.valueOf(19.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(35.0),
                BigDecimal.valueOf(45.0),
                200
            )
        );
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when environment name is blank")
    void updateInfo_whenEnvironmentNameIsBlank_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                EnvironmentType.DRYROOM,
                "ENV-999",
                " ",
                BigDecimal.valueOf(19.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(35.0),
                BigDecimal.valueOf(45.0),
                200
            )
        );
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when temperature range is invalid")
    void updateInfo_whenTempRangeIsInvalid_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                EnvironmentType.DRYROOM,
                "ENV-999",
                "Clean Room",
                BigDecimal.valueOf(27.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(35.0),
                BigDecimal.valueOf(45.0),
                200
            )
        );
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when humidity range is invalid")
    void updateInfo_whenHumidityRangeIsInvalid_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                EnvironmentType.DRYROOM,
                "ENV-999",
                "Clean Room",
                BigDecimal.valueOf(19.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(45.0),
                200
            )
        );
    }

    @Test
    @DisplayName("Update environment standard failure: throw exception when particle limit is null")
    void updateInfo_whenParticleLimitIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentStandard.updateInfo(
                EnvironmentType.DRYROOM,
                "ENV-999",
                "Clean Room",
                BigDecimal.valueOf(19.0),
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(35.0),
                BigDecimal.valueOf(45.0),
                null
            )
        );
    }
}
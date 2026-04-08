package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnvironmentEventTest {

    private EnvironmentEvent environmentEvent;

    @BeforeEach
    void setUp() {
        environmentEvent = EnvironmentEvent.builder()
            .environmentEventId(4001L)
            .equipmentId(5001L)
            .envTemperature(BigDecimal.valueOf(24.0))
            .envHumidity(BigDecimal.valueOf(38.0))
            .envParticleCnt(90)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(LocalDateTime.of(2026, 3, 31, 9, 0))
            .build();
    }

    @Test
    @DisplayName("Update environment event success: fields are updated correctly")
    void updateInfo_success() {
        LocalDateTime detectedAt = LocalDateTime.of(2026, 3, 31, 10, 30);

        environmentEvent.updateInfo(
            5002L,
            BigDecimal.valueOf(26.0),
            BigDecimal.valueOf(41.0),
            120,
            EnvDeviationType.HUMIDITY_DEVIATION,
            true,
            detectedAt
        );

        assertEquals(5002L, environmentEvent.getEquipmentId());
        assertEquals(BigDecimal.valueOf(26.0), environmentEvent.getEnvTemperature());
        assertEquals(BigDecimal.valueOf(41.0), environmentEvent.getEnvHumidity());
        assertEquals(120, environmentEvent.getEnvParticleCnt());
        assertEquals(EnvDeviationType.HUMIDITY_DEVIATION, environmentEvent.getEnvDeviationType());
        assertEquals(true, environmentEvent.getEnvCorrectionApplied());
        assertEquals(detectedAt, environmentEvent.getEnvDetectedAt());
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when equipment id is null")
    void updateInfo_whenEquipmentIdIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentEvent.updateInfo(
                null,
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(41.0),
                120,
                EnvDeviationType.HUMIDITY_DEVIATION,
                true,
                LocalDateTime.of(2026, 3, 31, 10, 30)
            )
        );
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when particle count is negative")
    void updateInfo_whenParticleCountIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentEvent.updateInfo(
                5002L,
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(41.0),
                -1,
                EnvDeviationType.HUMIDITY_DEVIATION,
                true,
                LocalDateTime.of(2026, 3, 31, 10, 30)
            )
        );
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when deviation type is null")
    void updateInfo_whenDeviationTypeIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentEvent.updateInfo(
                5002L,
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(41.0),
                120,
                null,
                true,
                LocalDateTime.of(2026, 3, 31, 10, 30)
            )
        );
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when correction applied is null")
    void updateInfo_whenCorrectionAppliedIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentEvent.updateInfo(
                5002L,
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(41.0),
                120,
                EnvDeviationType.HUMIDITY_DEVIATION,
                null,
                LocalDateTime.of(2026, 3, 31, 10, 30)
            )
        );
    }

    @Test
    @DisplayName("Update environment event failure: throw exception when detected time is null")
    void updateInfo_whenDetectedAtIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            environmentEvent.updateInfo(
                5002L,
                BigDecimal.valueOf(26.0),
                BigDecimal.valueOf(41.0),
                120,
                EnvDeviationType.HUMIDITY_DEVIATION,
                true,
                null
            )
        );
    }
}
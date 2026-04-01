package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class EnvironmentEventQueryMapperTest {

    @Autowired
    private EnvironmentEventQueryMapper environmentEventQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long equipmentId;
    private Long environmentEventId;
    private String equipmentCode;
    private String equipmentName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());

        Long factoryLineId = idGenerator.generate();
        Long equipmentProcessId = idGenerator.generate();
        Long environmentStandardId = idGenerator.generate();
        equipmentId = idGenerator.generate();
        environmentEventId = idGenerator.generate();

        equipmentCode = "EQ-EVENT-" + uniqueSuffix;
        equipmentName = "Event Equipment " + uniqueSuffix;

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            factoryLineId, "LINE-EVENT-" + uniqueSuffix, "Event Line " + uniqueSuffix, false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name, is_deleted) VALUES (?, ?, ?, ?, ?)",
            equipmentProcessId, factoryLineId, "PROC-EVENT-" + uniqueSuffix, "Event Process " + uniqueSuffix, false
        );

        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            environmentStandardId, "DRYROOM", "ENV-EVENT-" + uniqueSuffix, "Event Environment " + uniqueSuffix,
            BigDecimal.valueOf(18.0), BigDecimal.valueOf(25.0), BigDecimal.valueOf(30.0), BigDecimal.valueOf(40.0), 1000, false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            equipmentId, equipmentProcessId, environmentStandardId, equipmentCode, equipmentName, "OPERATING", "S", "Environment event mapper equipment"
        );

        jdbcTemplate.update(
            "INSERT INTO environment_event (environment_event_id, equipment_id, env_temperature, env_humidity, env_particle_cnt, env_deviation_type, env_correction_applied, env_detected_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            environmentEventId, equipmentId, BigDecimal.valueOf(24.0), BigDecimal.valueOf(38.0), 90, "TEMPERATURE_DEVIATION", false, LocalDateTime.of(2026, 4, 1, 9, 0)
        );

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @Test
    @DisplayName("select environment event list success")
    void selectEnvironmentEventList_success() {
        List<EnvironmentEventQueryResponse> result = environmentEventQueryMapper.selectEnvironmentEventList(EnvironmentEventSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        EnvironmentEventQueryResponse target = result.stream()
            .filter(item -> environmentEventId.equals(item.getEnvironmentEventId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(equipmentId, target.getEquipmentId());
        assertEquals(equipmentCode, target.getEquipmentCode());
        assertEquals(equipmentName, target.getEquipmentName());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, target.getEnvDeviationType());
    }

    @Test
    @DisplayName("select environment event list by equipment id success")
    void selectEnvironmentEventList_byEquipmentId_success() {
        EnvironmentEventSearchRequest request = EnvironmentEventSearchRequest.builder()
            .equipmentId(equipmentId)
            .build();

        List<EnvironmentEventQueryResponse> result = environmentEventQueryMapper.selectEnvironmentEventList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> equipmentId.equals(item.getEquipmentId())));
        assertTrue(result.stream().anyMatch(item -> environmentEventId.equals(item.getEnvironmentEventId())));
    }

    @Test
    @DisplayName("select environment event list by deviation type success")
    void selectEnvironmentEventList_byDeviationType_success() {
        EnvironmentEventSearchRequest request = EnvironmentEventSearchRequest.builder()
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .build();

        List<EnvironmentEventQueryResponse> result = environmentEventQueryMapper.selectEnvironmentEventList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> item.getEnvDeviationType() == EnvDeviationType.TEMPERATURE_DEVIATION));
    }

    @Test
    @DisplayName("select environment event detail by id success")
    void selectEnvironmentEventDetailById_success() {
        EnvironmentEventDetailResponse result = environmentEventQueryMapper.selectEnvironmentEventDetailById(environmentEventId);

        assertNotNull(result);
        assertEquals(environmentEventId, result.getEnvironmentEventId());
        assertEquals(equipmentId, result.getEquipmentId());
        assertEquals(equipmentCode, result.getEquipmentCode());
        assertEquals(equipmentName, result.getEquipmentName());
        assertEquals(EnvDeviationType.TEMPERATURE_DEVIATION, result.getEnvDeviationType());
    }

    @Test
    @DisplayName("select environment event detail by id when unknown id then null")
    void selectEnvironmentEventDetailById_whenUnknownId_thenNull() {
        EnvironmentEventDetailResponse result = environmentEventQueryMapper.selectEnvironmentEventDetailById(-1L);

        assertNull(result);
    }
}
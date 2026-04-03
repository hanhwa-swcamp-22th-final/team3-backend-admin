package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineEquipmentStatsResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FactoryLineQueryMapperTest {

    @Autowired
    private FactoryLineQueryMapper factoryLineQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long factoryLineId;
    private Long equipmentProcessId;
    private Long environmentStandardId;
    private String factoryLineCode;
    private String factoryLineName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        factoryLineId = idGenerator.generate();
        equipmentProcessId = idGenerator.generate();
        environmentStandardId = idGenerator.generate();
        factoryLineCode = "LINE-MAPPER-" + uniqueSuffix;
        factoryLineName = "Mapper Line " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            factoryLineId,
            factoryLineCode,
            factoryLineName,
            false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name) VALUES (?, ?, ?, ?)",
            equipmentProcessId,
            factoryLineId,
            "PROC-LINE-" + uniqueSuffix,
            "Line Process " + uniqueSuffix
        );

        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            environmentStandardId,
            "DRYROOM",
            "ENV-LINE-" + uniqueSuffix,
            "Line Environment " + uniqueSuffix,
            20.0,
            25.0,
            30.0,
            40.0,
            1000
        );
    }

    @Test
    @DisplayName("select factory line list success")
    void selectFactoryLineList_success() {
        List<FactoryLineQueryResponse> result = factoryLineQueryMapper.selectFactoryLineList(FactoryLineSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        FactoryLineQueryResponse target = result.stream()
            .filter(item -> factoryLineId.equals(item.getFactoryLineId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(factoryLineCode, target.getFactoryLineCode());
        assertEquals(factoryLineName, target.getFactoryLineName());
    }

    @Test
    @DisplayName("select factory line list excludes deleted rows")
    void selectFactoryLineList_excludesDeletedRows() {
        Long deletedFactoryLineId = idGenerator.generate();
        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            deletedFactoryLineId,
            "LINE-DEL-" + uniqueSuffix,
            "Deleted Line " + uniqueSuffix,
            true
        );

        List<FactoryLineQueryResponse> result = factoryLineQueryMapper.selectFactoryLineList(FactoryLineSearchRequest.builder().build());

        assertTrue(result.stream().noneMatch(item -> deletedFactoryLineId.equals(item.getFactoryLineId())));
    }

    @Test
    @DisplayName("select factory line detail by id success")
    void selectFactoryLineDetailById_success() {
        FactoryLineDetailResponse result = factoryLineQueryMapper.selectFactoryLineDetailById(factoryLineId);

        assertNotNull(result);
        assertEquals(factoryLineId, result.getFactoryLineId());
        assertEquals(factoryLineCode, result.getFactoryLineCode());
        assertEquals(factoryLineName, result.getFactoryLineName());
    }

    @Test
    @DisplayName("select factory line detail by id not found returns null when deleted")
    void selectFactoryLineDetailById_whenDeleted_thenNull() {
        jdbcTemplate.update("UPDATE factory_line SET is_deleted = true WHERE factory_line_id = ?", factoryLineId);

        FactoryLineDetailResponse result = factoryLineQueryMapper.selectFactoryLineDetailById(factoryLineId);

        assertNull(result);
    }

    @Test
    @DisplayName("select factory line equipment stats success")
    void selectFactoryLineEquipmentStats_success() {
        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            idGenerator.generate(),
            equipmentProcessId,
            environmentStandardId,
            "EQ-LINE-OP-" + uniqueSuffix,
            "Operating Equipment " + uniqueSuffix,
            "OPERATING",
            "S",
            "Operating equipment"
        );

        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            idGenerator.generate(),
            equipmentProcessId,
            environmentStandardId,
            "EQ-LINE-ST-" + uniqueSuffix,
            "Stopped Equipment " + uniqueSuffix,
            "STOPPED",
            "A",
            "Stopped equipment"
        );

        FactoryLineEquipmentStatsResponse result = factoryLineQueryMapper.selectFactoryLineEquipmentStats(factoryLineId);

        assertNotNull(result);
        assertEquals(2L, result.getTotalEquipmentCount());
        assertEquals(1L, result.getOperatingEquipmentCount());
        assertEquals("50.00", result.getOperationRate().toPlainString());
    }

    @Test
    @DisplayName("select factory line id by code success")
    void selectFactoryLineIdByCode_success() {
        Long result = factoryLineQueryMapper.selectFactoryLineIdByCode(factoryLineCode);

        assertEquals(factoryLineId, result);
    }
}

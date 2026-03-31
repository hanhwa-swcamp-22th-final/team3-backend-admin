package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
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
class EquipmentProcessQueryMapperTest {

    @Autowired
    private EquipmentProcessQueryMapper equipmentProcessQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long factoryLineId;
    private Long equipmentProcessId;
    private String factoryLineCode;
    private String factoryLineName;
    private String equipmentProcessCode;
    private String equipmentProcessName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        factoryLineId = idGenerator.generate();
        equipmentProcessId = idGenerator.generate();
        factoryLineCode = "LINE-EP-" + uniqueSuffix;
        factoryLineName = "EP Line " + uniqueSuffix;
        equipmentProcessCode = "PROC-MAPPER-" + uniqueSuffix;
        equipmentProcessName = "Mapper Process " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            factoryLineId,
            factoryLineCode,
            factoryLineName,
            false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name, is_deleted) VALUES (?, ?, ?, ?, ?)",
            equipmentProcessId,
            factoryLineId,
            equipmentProcessCode,
            equipmentProcessName,
            false
        );
    }

    @Test
    @DisplayName("select equipment process list success")
    void selectEquipmentProcessList_success() {
        List<EquipmentProcessQueryResponse> result = equipmentProcessQueryMapper.selectEquipmentProcessList(EquipmentProcessSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        EquipmentProcessQueryResponse target = result.stream()
            .filter(item -> equipmentProcessId.equals(item.getEquipmentProcessId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(factoryLineId, target.getFactoryLineId());
        assertEquals(factoryLineCode, target.getFactoryLineCode());
        assertEquals(factoryLineName, target.getFactoryLineName());
        assertEquals(equipmentProcessCode, target.getEquipmentProcessCode());
        assertEquals(equipmentProcessName, target.getEquipmentProcessName());
    }

    @Test
    @DisplayName("select equipment process list by factory line id success")
    void selectEquipmentProcessList_byFactoryLineId_success() {
        List<EquipmentProcessQueryResponse> result = equipmentProcessQueryMapper.selectEquipmentProcessList(
            EquipmentProcessSearchRequest.builder().factoryLineId(factoryLineId).build()
        );

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> factoryLineId.equals(item.getFactoryLineId())));
    }

    @Test
    @DisplayName("select equipment process list excludes deleted rows")
    void selectEquipmentProcessList_excludesDeletedRows() {
        Long deletedEquipmentProcessId = idGenerator.generate();
        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name, is_deleted) VALUES (?, ?, ?, ?, ?)",
            deletedEquipmentProcessId,
            factoryLineId,
            "PROC-DEL-" + uniqueSuffix,
            "Deleted Process " + uniqueSuffix,
            true
        );

        List<EquipmentProcessQueryResponse> result = equipmentProcessQueryMapper.selectEquipmentProcessList(EquipmentProcessSearchRequest.builder().build());

        assertTrue(result.stream().noneMatch(item -> deletedEquipmentProcessId.equals(item.getEquipmentProcessId())));
    }

    @Test
    @DisplayName("select equipment process detail by id success")
    void selectEquipmentProcessDetailById_success() {
        EquipmentProcessDetailResponse result = equipmentProcessQueryMapper.selectEquipmentProcessDetailById(equipmentProcessId);

        assertNotNull(result);
        assertEquals(equipmentProcessId, result.getEquipmentProcessId());
        assertEquals(factoryLineId, result.getFactoryLineId());
        assertEquals(factoryLineCode, result.getFactoryLineCode());
        assertEquals(factoryLineName, result.getFactoryLineName());
        assertEquals(equipmentProcessCode, result.getEquipmentProcessCode());
        assertEquals(equipmentProcessName, result.getEquipmentProcessName());
    }

    @Test
    @DisplayName("select equipment process detail by id not found returns null when deleted")
    void selectEquipmentProcessDetailById_whenDeleted_thenNull() {
        jdbcTemplate.update("UPDATE equipment_process SET is_deleted = true WHERE equipment_process_id = ?", equipmentProcessId);

        EquipmentProcessDetailResponse result = equipmentProcessQueryMapper.selectEquipmentProcessDetailById(equipmentProcessId);

        assertNull(result);
    }

    @Test
    @DisplayName("select equipment process id by code success")
    void selectEquipmentProcessIdByCode_success() {
        Long result = equipmentProcessQueryMapper.selectEquipmentProcessIdByCode(equipmentProcessCode);

        assertEquals(equipmentProcessId, result);
    }
}

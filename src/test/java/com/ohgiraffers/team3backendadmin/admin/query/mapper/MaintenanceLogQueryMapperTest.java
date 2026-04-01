package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class MaintenanceLogQueryMapperTest {

    @Autowired
    private MaintenanceLogQueryMapper maintenanceLogQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long equipmentId;
    private Long maintenanceLogId;
    private Long maintenanceItemStandardId;
    private String equipmentCode;
    private String equipmentName;
    private String maintenanceItem;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());

        Long factoryLineId = idGenerator.generate();
        Long equipmentProcessId = idGenerator.generate();
        Long environmentStandardId = idGenerator.generate();
        equipmentId = idGenerator.generate();
        maintenanceItemStandardId = idGenerator.generate();
        maintenanceLogId = idGenerator.generate();

        equipmentCode = "EQ-MAINT-" + uniqueSuffix;
        equipmentName = "Maintenance Mapper Equipment " + uniqueSuffix;
        maintenanceItem = "Bearing Inspection " + uniqueSuffix;

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            factoryLineId, "LINE-MAINT-" + uniqueSuffix, "Maintenance Line " + uniqueSuffix, false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name, is_deleted) VALUES (?, ?, ?, ?, ?)",
            equipmentProcessId, factoryLineId, "PROC-MAINT-" + uniqueSuffix, "Maintenance Process " + uniqueSuffix, false
        );

        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            environmentStandardId, "DRYROOM", "ENV-MAINT-" + uniqueSuffix, "Maintenance Environment " + uniqueSuffix,
            BigDecimal.valueOf(18), BigDecimal.valueOf(25), BigDecimal.valueOf(30), BigDecimal.valueOf(40), 1000, false
        );

        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            equipmentId, equipmentProcessId, environmentStandardId, equipmentCode, equipmentName, "OPERATING", "S", "Maintenance mapper equipment"
        );

        jdbcTemplate.update(
            "INSERT INTO maintenance_item_standard (maintenance_item_standard_id, maintenance_item, maintenance_weight, maintenance_score_max) VALUES (?, ?, ?, ?)",
            maintenanceItemStandardId, maintenanceItem, BigDecimal.valueOf(2), BigDecimal.valueOf(10)
        );

        jdbcTemplate.update(
            "INSERT INTO maintenance_log (maintenance_log_id, equipment_id, maintenance_item_standard_id, maintenance_type, maintenance_date, maintenance_score, eta_maint_delta, maintenance_result) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            maintenanceLogId, equipmentId, maintenanceItemStandardId, "REGULAR", LocalDate.of(2026, 3, 31), BigDecimal.valueOf(9), BigDecimal.valueOf(1), "NORMAL"
        );

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @Test
    @DisplayName("select maintenance log list success")
    void selectMaintenanceLogList_success() {
        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryMapper.selectMaintenanceLogList(MaintenanceLogSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        MaintenanceLogQueryResponse target = result.stream()
            .filter(item -> maintenanceLogId.equals(item.getMaintenanceLogId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(equipmentId, target.getEquipmentId());
        assertEquals(equipmentCode, target.getEquipmentCode());
        assertEquals(equipmentName, target.getEquipmentName());
        assertEquals(maintenanceItemStandardId, target.getMaintenanceItemStandardId());
        assertEquals(maintenanceItem, target.getMaintenanceItem());
        assertEquals(MaintenanceType.REGULAR, target.getMaintenanceType());
        assertEquals(MaintenanceResult.NORMAL, target.getMaintenanceResult());
        assertEquals(BigDecimal.valueOf(1), target.getEtaMaintDelta());
    }

    @Test
    @DisplayName("select maintenance log list by equipment id success")
    void selectMaintenanceLogList_byEquipmentId_success() {
        MaintenanceLogSearchRequest request = MaintenanceLogSearchRequest.builder()
            .equipmentId(equipmentId)
            .build();

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryMapper.selectMaintenanceLogList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> equipmentId.equals(item.getEquipmentId())));
        assertTrue(result.stream().anyMatch(item -> maintenanceLogId.equals(item.getMaintenanceLogId())));
    }

    @Test
    @DisplayName("select maintenance log list by maintenance type success")
    void selectMaintenanceLogList_byMaintenanceType_success() {
        MaintenanceLogSearchRequest request = MaintenanceLogSearchRequest.builder()
            .maintenanceType(MaintenanceType.REGULAR)
            .build();

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryMapper.selectMaintenanceLogList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> item.getMaintenanceType() == MaintenanceType.REGULAR));
    }

    @Test
    @DisplayName("select maintenance log list by maintenance result success")
    void selectMaintenanceLogList_byMaintenanceResult_success() {
        MaintenanceLogSearchRequest request = MaintenanceLogSearchRequest.builder()
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryMapper.selectMaintenanceLogList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> item.getMaintenanceResult() == MaintenanceResult.NORMAL));
    }

    @Test
    @DisplayName("select maintenance log detail by id success")
    void selectMaintenanceLogDetailById_success() {
        MaintenanceLogDetailResponse result = maintenanceLogQueryMapper.selectMaintenanceLogDetailById(maintenanceLogId);

        assertNotNull(result);
        assertEquals(maintenanceLogId, result.getMaintenanceLogId());
        assertEquals(equipmentId, result.getEquipmentId());
        assertEquals(equipmentCode, result.getEquipmentCode());
        assertEquals(equipmentName, result.getEquipmentName());
        assertEquals(maintenanceItemStandardId, result.getMaintenanceItemStandardId());
        assertEquals(maintenanceItem, result.getMaintenanceItem());
        assertEquals(MaintenanceType.REGULAR, result.getMaintenanceType());
        assertEquals(MaintenanceResult.NORMAL, result.getMaintenanceResult());
        assertEquals(BigDecimal.valueOf(1), result.getEtaMaintDelta());
    }

    @Test
    @DisplayName("select maintenance log detail by id when unknown id then null")
    void selectMaintenanceLogDetailById_whenUnknownId_thenNull() {
        MaintenanceLogDetailResponse result = maintenanceLogQueryMapper.selectMaintenanceLogDetailById(-1L);

        assertNull(result);
    }
}
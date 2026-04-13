package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentSummaryQueryResponse;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class EquipmentQueryMapperTest {

    @Autowired
    private EquipmentQueryMapper equipmentQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long factoryLineId;
    private Long equipmentProcessId;
    private Long environmentStandardId;
    private Long equipmentId;
    private Long equipmentAgingParamId;
    private Long equipmentBaselineId;
    private String equipmentCode;
    private String equipmentName;
    private String equipmentProcessName;
    private String factoryLineName;
    private String environmentCode;
    private String environmentName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());

        factoryLineId = idGenerator.generate();
        equipmentProcessId = idGenerator.generate();
        environmentStandardId = idGenerator.generate();
        equipmentId = idGenerator.generate();
        equipmentAgingParamId = idGenerator.generate();
        equipmentBaselineId = idGenerator.generate();

        factoryLineName = "Mapper Line " + uniqueSuffix;
        equipmentProcessName = "Mapper Process " + uniqueSuffix;
        environmentCode = "ENV-MAPPER-" + uniqueSuffix;
        environmentName = "Mapper Environment " + uniqueSuffix;
        equipmentCode = "EQ-MAPPER-" + uniqueSuffix;
        equipmentName = "Mapper Equipment " + uniqueSuffix;

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name) VALUES (?, ?, ?)",
            factoryLineId,
            "LINE-MAPPER-" + uniqueSuffix,
            factoryLineName
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_process (equipment_process_id, factory_line_id, equipment_process_code, equipment_process_name) VALUES (?, ?, ?, ?)",
            equipmentProcessId,
            factoryLineId,
            "PROC-MAPPER-" + uniqueSuffix,
            equipmentProcessName
        );

        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            environmentStandardId,
            "DRYROOM",
            environmentCode,
            environmentName,
            BigDecimal.valueOf(20.0),
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            BigDecimal.valueOf(40.0),
            1000
        );

        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            equipmentId,
            equipmentProcessId,
            environmentStandardId,
            equipmentCode,
            equipmentName,
            "OPERATING",
            "S",
            "Mapper test equipment"
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_aging_param (equipment_aging_param_id, equipment_id, equipment_warranty_month, equipment_design_life_months, equipment_wear_coefficient) VALUES (?, ?, ?, ?, ?)",
            equipmentAgingParamId,
            equipmentId,
            24,
            120,
            BigDecimal.valueOf(0.75)
        );

        jdbcTemplate.update(
            "INSERT INTO equipment_baseline (equipment_baseline_id, equipment_id, equipment_aging_param_id) VALUES (?, ?, ?)",
            equipmentBaselineId,
            equipmentId,
            equipmentAgingParamId
        );

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @Test
    @DisplayName("select equipment list success")
    void selectEquipmentList_success() {
        List<EquipmentQueryResponse> result = equipmentQueryMapper.selectEquipmentList(EquipmentSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        EquipmentQueryResponse target = result.stream()
            .filter(item -> equipmentId.equals(item.getEquipmentId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(equipmentCode, target.getEquipmentCode());
        assertEquals(equipmentName, target.getEquipmentName());
        assertEquals(EquipmentStatus.OPERATING, target.getEquipmentStatus());
        assertEquals(EquipmentGrade.S, target.getEquipmentGrade());
        assertEquals(equipmentProcessName, target.getEquipmentProcessName());
        assertEquals(factoryLineName, target.getFactoryLineName());
        assertEquals(environmentName, target.getEnvironmentName());
    }

    @Test
    @DisplayName("select equipment list with keyword success")
    void selectEquipmentList_withKeyword_success() {
        EquipmentSearchRequest request = EquipmentSearchRequest.builder()
            .keyword(uniqueSuffix)
            .build();

        List<EquipmentQueryResponse> result = equipmentQueryMapper.selectEquipmentList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> equipmentId.equals(item.getEquipmentId())));
        assertTrue(result.stream().allMatch(item -> item.getEquipmentCode().contains(uniqueSuffix) || item.getEquipmentName().contains(uniqueSuffix)));
    }

    @Test
    @DisplayName("select equipment list with status filter success")
    void selectEquipmentList_withStatusFilter_success() {
        Long stoppedEquipmentId = idGenerator.generate();

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            stoppedEquipmentId,
            equipmentProcessId,
            environmentStandardId,
            "EQ-MAPPER-STOP-" + uniqueSuffix,
            "Mapper Stopped Equipment " + uniqueSuffix,
            "STOPPED",
            "B",
            "Stopped status equipment"
        );
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        EquipmentSearchRequest request = EquipmentSearchRequest.builder()
            .keyword(uniqueSuffix)
            .equipmentStatus(EquipmentStatus.STOPPED)
            .build();

        List<EquipmentQueryResponse> result = equipmentQueryMapper.selectEquipmentList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(item -> item.getEquipmentStatus() == EquipmentStatus.STOPPED));
        assertTrue(result.stream().anyMatch(item -> stoppedEquipmentId.equals(item.getEquipmentId())));
        assertFalse(result.stream().anyMatch(item -> equipmentId.equals(item.getEquipmentId())));
    }

    @Test
    @DisplayName("select equipment summary success")
    void selectEquipmentSummary_success() {
        Long stoppedEquipmentId = idGenerator.generate();
        Long inspectionEquipmentId = idGenerator.generate();
        Long disposedEquipmentId = idGenerator.generate();

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            stoppedEquipmentId,
            equipmentProcessId,
            environmentStandardId,
            "EQ-SUM-STOP-" + uniqueSuffix,
            "Summary Stopped " + uniqueSuffix,
            "STOPPED",
            "A",
            "Stopped equipment"
        );
        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            inspectionEquipmentId,
            equipmentProcessId,
            environmentStandardId,
            "EQ-SUM-INSP-" + uniqueSuffix,
            "Summary Inspection " + uniqueSuffix,
            "UNDER_INSPECTION",
            "B",
            "Inspection equipment"
        );
        jdbcTemplate.update(
            "INSERT INTO equipment (equipment_id, equipment_process_id, environment_standard_id, equipment_code, equipment_name, equipment_status, equipment_grade, equipment_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            disposedEquipmentId,
            equipmentProcessId,
            environmentStandardId,
            "EQ-SUM-DISP-" + uniqueSuffix,
            "Summary Disposed " + uniqueSuffix,
            "DISPOSED",
            "C",
            "Disposed equipment"
        );
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        EquipmentSummaryQueryResponse result = equipmentQueryMapper.selectEquipmentSummary();

        assertNotNull(result);
        assertTrue(result.getTotalCount() >= 4L);
        assertTrue(result.getOperatingCount() >= 1L);
        assertTrue(result.getStoppedCount() >= 1L);
        assertTrue(result.getUnderInspectionCount() >= 1L);
        assertTrue(result.getDisposedCount() >= 1L);
    }

    @Test
    @DisplayName("select equipment detail by id success")
    void selectEquipmentDetailById_success() {
        EquipmentDetailResponse result = equipmentQueryMapper.selectEquipmentDetailById(equipmentId);

        assertNotNull(result);
        assertEquals(equipmentId, result.getEquipmentId());
        assertEquals(equipmentCode, result.getEquipmentCode());
        assertEquals(equipmentName, result.getEquipmentName());
        assertEquals(EquipmentStatus.OPERATING, result.getEquipmentStatus());
        assertEquals(EquipmentGrade.S, result.getEquipmentGrade());
        assertEquals(equipmentProcessName, result.getEquipmentProcessName());
        assertEquals(factoryLineName, result.getFactoryLineName());
        assertEquals(environmentCode, result.getEnvironmentCode());
        assertEquals(environmentName, result.getEnvironmentName());
    }

    @Test
    @DisplayName("select equipment detail by id not found returns null")
    void selectEquipmentDetailById_whenUnknownId_thenNull() {
        EquipmentDetailResponse result = equipmentQueryMapper.selectEquipmentDetailById(-1L);

        assertNull(result);
    }

    @Test
    @DisplayName("select equipment detail by id returns null when equipment is soft deleted")
    void selectEquipmentDetailById_whenSoftDeleted_thenNull() {
        jdbcTemplate.update("UPDATE equipment SET is_deleted = true WHERE equipment_id = ?", equipmentId);

        EquipmentDetailResponse result = equipmentQueryMapper.selectEquipmentDetailById(equipmentId);

        assertNull(result);
    }

    @Test
    @DisplayName("select equipment id by code success")
    void selectEquipmentIdByCode_success() {
        Long result = equipmentQueryMapper.selectEquipmentIdByCode(equipmentCode);

        assertEquals(equipmentId, result);
    }

    @Test
    @DisplayName("select equipment aging param id by equipment id success")
    void selectEquipmentAgingParamIdByEquipmentId_success() {
        Long result = equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(equipmentId);

        assertEquals(equipmentAgingParamId, result);
    }

    @Test
    @DisplayName("select equipment aging param id by equipment id not found returns null")
    void selectEquipmentAgingParamIdByEquipmentId_whenUnknownId_thenNull() {
        Long result = equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(-1L);

        assertNull(result);
    }

    @Test
    @DisplayName("select equipment baseline id by equipment id success")
    void selectEquipmentBaselineIdByEquipmentId_success() {
        Long result = equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(equipmentId);

        assertEquals(equipmentBaselineId, result);
    }

    @Test
    @DisplayName("select equipment baseline id by equipment id not found returns null")
    void selectEquipmentBaselineIdByEquipmentId_whenUnknownId_thenNull() {
        Long result = equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(-1L);

        assertNull(result);
    }
}

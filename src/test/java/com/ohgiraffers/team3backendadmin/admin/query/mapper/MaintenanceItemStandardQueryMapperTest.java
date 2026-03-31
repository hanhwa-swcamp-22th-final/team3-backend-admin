package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardQueryResponse;
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
class MaintenanceItemStandardQueryMapperTest {

    @Autowired
    private MaintenanceItemStandardQueryMapper maintenanceItemStandardQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long maintenanceItemStandardId;
    private String maintenanceItem;
    private Long deletedMaintenanceItemStandardId;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        maintenanceItemStandardId = idGenerator.generate();
        deletedMaintenanceItemStandardId = idGenerator.generate();
        maintenanceItem = "Bearing Inspection " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO maintenance_item_standard (maintenance_item_standard_id, maintenance_item, maintenance_weight, maintenance_score_max, is_deleted) VALUES (?, ?, ?, ?, ?)",
            maintenanceItemStandardId,
            maintenanceItem,
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(10),
            false
        );

        jdbcTemplate.update(
            "INSERT INTO maintenance_item_standard (maintenance_item_standard_id, maintenance_item, maintenance_weight, maintenance_score_max, is_deleted) VALUES (?, ?, ?, ?, ?)",
            deletedMaintenanceItemStandardId,
            "Deleted Item " + uniqueSuffix,
            BigDecimal.valueOf(1),
            BigDecimal.valueOf(5),
            true
        );
    }

    @Test
    @DisplayName("select maintenance item standard list success")
    void selectMaintenanceItemStandardList_success() {
        List<MaintenanceItemStandardQueryResponse> result = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(MaintenanceItemStandardSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        MaintenanceItemStandardQueryResponse target = result.stream()
            .filter(item -> maintenanceItemStandardId.equals(item.getMaintenanceItemStandardId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(maintenanceItem, target.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(2), target.getMaintenanceWeight());
        assertEquals(BigDecimal.valueOf(10), target.getMaintenanceScoreMax());
    }

    @Test
    @DisplayName("select maintenance item standard list with keyword success")
    void selectMaintenanceItemStandardList_withKeyword_success() {
        MaintenanceItemStandardSearchRequest request = MaintenanceItemStandardSearchRequest.builder()
            .keyword(uniqueSuffix)
            .build();

        List<MaintenanceItemStandardQueryResponse> result = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> maintenanceItemStandardId.equals(item.getMaintenanceItemStandardId())));
        assertTrue(result.stream().allMatch(item -> item.getMaintenanceItem().contains(uniqueSuffix)));
    }

    @Test
    @DisplayName("select maintenance item standard detail by id success")
    void selectMaintenanceItemStandardDetailById_success() {
        MaintenanceItemStandardDetailResponse result = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(maintenanceItemStandardId);

        assertNotNull(result);
        assertEquals(maintenanceItemStandardId, result.getMaintenanceItemStandardId());
        assertEquals(maintenanceItem, result.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(2), result.getMaintenanceWeight());
    }

    @Test
    @DisplayName("select maintenance item standard detail by id when unknown id then null")
    void selectMaintenanceItemStandardDetailById_whenUnknownId_thenNull() {
        MaintenanceItemStandardDetailResponse result = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(-1L);

        assertNull(result);
    }

    @Test
    @DisplayName("select maintenance item standard list excludes deleted rows")
    void selectMaintenanceItemStandardList_excludesDeletedRows() {
        List<MaintenanceItemStandardQueryResponse> result = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(
            MaintenanceItemStandardSearchRequest.builder().build()
        );

        assertTrue(result.stream().noneMatch(item -> deletedMaintenanceItemStandardId.equals(item.getMaintenanceItemStandardId())));
    }

    @Test
    @DisplayName("select maintenance item standard detail by id when deleted then null")
    void selectMaintenanceItemStandardDetailById_whenDeleted_thenNull() {
        MaintenanceItemStandardDetailResponse result =
            maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(deletedMaintenanceItemStandardId);

        assertNull(result);
    }
}

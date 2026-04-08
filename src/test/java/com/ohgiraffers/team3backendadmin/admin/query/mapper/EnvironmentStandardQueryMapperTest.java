package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardQueryResponse;
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
class EnvironmentStandardQueryMapperTest {

    @Autowired
    private EnvironmentStandardQueryMapper environmentStandardQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long environmentStandardId;
    private String environmentCode;
    private String environmentName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        environmentStandardId = idGenerator.generate();
        environmentCode = "ENV-MAPPER-" + uniqueSuffix;
        environmentName = "Mapper Environment " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            environmentStandardId,
            "DRYROOM",
            environmentCode,
            environmentName,
            BigDecimal.valueOf(18.0),
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            BigDecimal.valueOf(40.0),
            1000,
            false
        );
    }

    @Test
    @DisplayName("select environment standard list success")
    void selectEnvironmentStandardList_success() {
        List<EnvironmentStandardQueryResponse> result = environmentStandardQueryMapper.selectEnvironmentStandardList(EnvironmentStandardSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        EnvironmentStandardQueryResponse target = result.stream()
            .filter(item -> environmentStandardId.equals(item.getEnvironmentStandardId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(EnvironmentType.DRYROOM, target.getEnvironmentType());
        assertEquals(environmentCode, target.getEnvironmentCode());
        assertEquals(environmentName, target.getEnvironmentName());
    }

    @Test
    @DisplayName("select environment standard list with keyword success")
    void selectEnvironmentStandardList_withKeyword_success() {
        EnvironmentStandardSearchRequest request = EnvironmentStandardSearchRequest.builder()
            .keyword(uniqueSuffix)
            .build();

        List<EnvironmentStandardQueryResponse> result = environmentStandardQueryMapper.selectEnvironmentStandardList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> environmentStandardId.equals(item.getEnvironmentStandardId())));
        assertTrue(result.stream().allMatch(item -> item.getEnvironmentCode().contains(uniqueSuffix) || item.getEnvironmentName().contains(uniqueSuffix)));
    }

    @Test
    @DisplayName("select environment standard list excludes deleted rows")
    void selectEnvironmentStandardList_excludesDeletedRows() {
        Long deletedId = idGenerator.generate();
        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            deletedId,
            "GENERAL",
            "ENV-DELETED-" + uniqueSuffix,
            "Deleted Environment " + uniqueSuffix,
            BigDecimal.valueOf(18.0),
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            BigDecimal.valueOf(40.0),
            500,
            true
        );

        List<EnvironmentStandardQueryResponse> result = environmentStandardQueryMapper.selectEnvironmentStandardList(EnvironmentStandardSearchRequest.builder().keyword(uniqueSuffix).build());

        assertFalse(result.stream().anyMatch(item -> deletedId.equals(item.getEnvironmentStandardId())));
    }

    @Test
    @DisplayName("select environment standard detail by id success")
    void selectEnvironmentStandardDetailById_success() {
        EnvironmentStandardDetailResponse result = environmentStandardQueryMapper.selectEnvironmentStandardDetailById(environmentStandardId);

        assertNotNull(result);
        assertEquals(environmentStandardId, result.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, result.getEnvironmentType());
        assertEquals(environmentCode, result.getEnvironmentCode());
        assertEquals(environmentName, result.getEnvironmentName());
    }

    @Test
    @DisplayName("select environment standard detail by id when deleted then null")
    void selectEnvironmentStandardDetailById_whenDeleted_thenNull() {
        Long deletedId = idGenerator.generate();
        jdbcTemplate.update(
            "INSERT INTO environment_standard (environment_standard_id, environment_type, environment_code, environment_name, env_temp_min, env_temp_max, env_humidity_min, env_humidity_max, env_particle_limit, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            deletedId,
            "GENERAL",
            "ENV-DELETED-DETAIL-" + uniqueSuffix,
            "Deleted Detail Environment " + uniqueSuffix,
            BigDecimal.valueOf(18.0),
            BigDecimal.valueOf(25.0),
            BigDecimal.valueOf(30.0),
            BigDecimal.valueOf(40.0),
            500,
            true
        );

        EnvironmentStandardDetailResponse result = environmentStandardQueryMapper.selectEnvironmentStandardDetailById(deletedId);

        assertNull(result);
    }

    @Test
    @DisplayName("select environment standard id by code success")
    void selectEnvironmentStandardIdByCode_success() {
        Long result = environmentStandardQueryMapper.selectEnvironmentStandardIdByCode(environmentCode);

        assertEquals(environmentStandardId, result);
    }
}
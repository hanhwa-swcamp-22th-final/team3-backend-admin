package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
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
    private String factoryLineCode;
    private String factoryLineName;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        factoryLineId = idGenerator.generate();
        factoryLineCode = "LINE-MAPPER-" + uniqueSuffix;
        factoryLineName = "Mapper Line " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO factory_line (factory_line_id, factory_line_code, factory_line_name, is_deleted) VALUES (?, ?, ?, ?)",
            factoryLineId,
            factoryLineCode,
            factoryLineName,
            false
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
    @DisplayName("select factory line id by code success")
    void selectFactoryLineIdByCode_success() {
        Long result = factoryLineQueryMapper.selectFactoryLineIdByCode(factoryLineCode);

        assertEquals(factoryLineId, result);
    }
}

package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.domainkeyword.DomainKeywordSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordQueryResponse;
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
class DomainKeywordQueryMapperTest {

    @Autowired
    private DomainKeywordQueryMapper domainKeywordQueryMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private String uniqueSuffix;
    private Long domainKeywordId;
    private Long inactiveDomainKeywordId;
    private String keyword;

    @BeforeEach
    void setUp() {
        uniqueSuffix = String.valueOf(idGenerator.generate());
        domainKeywordId = idGenerator.generate();
        inactiveDomainKeywordId = idGenerator.generate();
        keyword = "안전 역량 " + uniqueSuffix;

        jdbcTemplate.update(
            "INSERT INTO domain_keyword (domain_keyword_id, domain_keyword, domain_keyword_description, domain_competency_category, domain_base_score, domain_weight, domain_is_active) VALUES (?, ?, ?, ?, ?, ?, ?)",
            domainKeywordId,
            keyword,
            "안전 수칙을 지키는 역량",
            DomainCompetencyCategory.SAFETY.name(),
            BigDecimal.valueOf(3),
            BigDecimal.valueOf(2),
            true
        );

        jdbcTemplate.update(
            "INSERT INTO domain_keyword (domain_keyword_id, domain_keyword, domain_keyword_description, domain_competency_category, domain_base_score, domain_weight, domain_is_active) VALUES (?, ?, ?, ?, ?, ?, ?)",
            inactiveDomainKeywordId,
            "비활성 키워드 " + uniqueSuffix,
            "비활성 상태 역량",
            DomainCompetencyCategory.SAFETY.name(),
            BigDecimal.valueOf(2),
            BigDecimal.valueOf(1),
            false
        );
    }

    @Test
    @DisplayName("select domain keyword list success")
    void selectDomainKeywordList_success() {
        List<DomainKeywordQueryResponse> result = domainKeywordQueryMapper.selectDomainKeywordList(DomainKeywordSearchRequest.builder().build());

        assertFalse(result.isEmpty());
        DomainKeywordQueryResponse target = result.stream()
            .filter(item -> domainKeywordId.equals(item.getDomainKeywordId()))
            .findFirst()
            .orElse(null);

        assertNotNull(target);
        assertEquals(keyword, target.getDomainKeyword());
        assertEquals("안전 수칙을 지키는 역량", target.getDomainKeywordDescription());
        assertEquals(DomainCompetencyCategory.SAFETY, target.getDomainCompetencyCategory());
    }

    @Test
    @DisplayName("select domain keyword list with keyword success")
    void selectDomainKeywordList_withKeyword_success() {
        DomainKeywordSearchRequest request = DomainKeywordSearchRequest.builder()
            .keyword(uniqueSuffix)
            .build();

        List<DomainKeywordQueryResponse> result = domainKeywordQueryMapper.selectDomainKeywordList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> domainKeywordId.equals(item.getDomainKeywordId())));
        assertTrue(result.stream().allMatch(item -> item.getDomainKeyword().contains(uniqueSuffix)));
    }

    @Test
    @DisplayName("select domain keyword list with category success")
    void selectDomainKeywordList_withCategory_success() {
        DomainKeywordSearchRequest request = DomainKeywordSearchRequest.builder()
            .category(DomainCompetencyCategory.SAFETY)
            .build();

        List<DomainKeywordQueryResponse> result = domainKeywordQueryMapper.selectDomainKeywordList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> domainKeywordId.equals(item.getDomainKeywordId())));
        assertTrue(result.stream().allMatch(item -> DomainCompetencyCategory.SAFETY.equals(item.getDomainCompetencyCategory())));
    }

    @Test
    @DisplayName("select domain keyword list with active filter success")
    void selectDomainKeywordList_withActiveFilter_success() {
        DomainKeywordSearchRequest request = DomainKeywordSearchRequest.builder()
            .isActive(true)
            .build();

        List<DomainKeywordQueryResponse> result = domainKeywordQueryMapper.selectDomainKeywordList(request);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(item -> domainKeywordId.equals(item.getDomainKeywordId())));
        assertTrue(result.stream().noneMatch(item -> inactiveDomainKeywordId.equals(item.getDomainKeywordId())));
        assertTrue(result.stream().allMatch(item -> Boolean.TRUE.equals(item.getDomainIsActive())));
    }

    @Test
    @DisplayName("select domain keyword detail by id success")
    void selectDomainKeywordDetailById_success() {
        DomainKeywordDetailResponse result = domainKeywordQueryMapper.selectDomainKeywordDetailById(domainKeywordId);

        assertNotNull(result);
        assertEquals(domainKeywordId, result.getDomainKeywordId());
        assertEquals(keyword, result.getDomainKeyword());
        assertEquals("안전 수칙을 지키는 역량", result.getDomainKeywordDescription());
    }

    @Test
    @DisplayName("select domain keyword detail by id when unknown id then null")
    void selectDomainKeywordDetailById_whenUnknownId_thenNull() {
        DomainKeywordDetailResponse result = domainKeywordQueryMapper.selectDomainKeywordDetailById(-1L);

        assertNull(result);
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainKeyword;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DomainKeywordRepositoryTest {

    @Autowired
    private DomainKeywordRepository domainKeywordRepository;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private DomainKeyword domainKeyword;
    private Long domainKeywordId;
    private String keyword;

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(idGenerator.generate());
        domainKeywordId = idGenerator.generate();
        keyword = "안전 준수 " + uniqueSuffix;

        domainKeyword = DomainKeyword.builder()
            .domainKeywordId(domainKeywordId)
            .domainKeyword(keyword)
            .domainKeywordDescription("안전 수칙 준수 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(BigDecimal.valueOf(4.5))
            .domainWeight(BigDecimal.valueOf(2.0))
            .domainIsActive(true)
            .build();
    }

    @Test
    @DisplayName("Save domain keyword success: domain keyword is persisted")
    void save_success() {
        DomainKeyword savedDomainKeyword = domainKeywordRepository.save(domainKeyword);

        assertNotNull(savedDomainKeyword);
        assertEquals(domainKeywordId, savedDomainKeyword.getDomainKeywordId());
        assertEquals(keyword, savedDomainKeyword.getDomainKeyword());
        assertEquals("안전 수칙 준수 역량", savedDomainKeyword.getDomainKeywordDescription());
        assertEquals(DomainCompetencyCategory.SAFETY, savedDomainKeyword.getDomainCompetencyCategory());
        assertTrue(savedDomainKeyword.getDomainIsActive());
    }

    @Test
    @DisplayName("Find domain keyword by id success: return persisted domain keyword")
    void findById_success() {
        domainKeywordRepository.save(domainKeyword);

        Optional<DomainKeyword> result = domainKeywordRepository.findById(domainKeywordId);

        assertTrue(result.isPresent());
        assertEquals(keyword, result.get().getDomainKeyword());
    }

    @Test
    @DisplayName("Find domain keyword by id failure: return empty when domain keyword does not exist")
    void findById_whenNotFound_thenEmpty() {
        Optional<DomainKeyword> result = domainKeywordRepository.findById(idGenerator.generate());

        assertFalse(result.isPresent());
    }
}

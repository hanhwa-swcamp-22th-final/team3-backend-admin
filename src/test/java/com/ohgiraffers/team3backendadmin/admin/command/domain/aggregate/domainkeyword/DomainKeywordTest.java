package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainKeywordTest {

    private DomainKeyword domainKeyword;

    @BeforeEach
    void setUp() {
        domainKeyword = DomainKeyword.builder()
            .domainKeywordId(1L)
            .domainKeyword("정비 대응")
            .domainKeywordDescription("설비 정비 상황에 즉시 대응하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.TECHNICAL_COMPETENCE)
            .domainBaseScore(BigDecimal.valueOf(5.0))
            .domainWeight(BigDecimal.valueOf(2.0))
            .domainIsActive(true)
            .build();
    }

    @Test
    @DisplayName("Update domain keyword success: change keyword attributes")
    void update_success() {
        domainKeyword.update(
            "설비 분석",
            "설비 이상 신호를 빠르게 식별하는 역량",
            DomainCompetencyCategory.INNOVATION,
            BigDecimal.valueOf(6.5),
            BigDecimal.valueOf(3.5),
            false
        );

        assertEquals("설비 분석", domainKeyword.getDomainKeyword());
        assertEquals("설비 이상 신호를 빠르게 식별하는 역량", domainKeyword.getDomainKeywordDescription());
        assertEquals(DomainCompetencyCategory.INNOVATION, domainKeyword.getDomainCompetencyCategory());
        assertEquals(BigDecimal.valueOf(6.5), domainKeyword.getDomainBaseScore());
        assertEquals(BigDecimal.valueOf(3.5), domainKeyword.getDomainWeight());
        assertFalse(domainKeyword.getDomainIsActive());
    }

    @Test
    @DisplayName("Update domain keyword failure: throw exception when keyword is blank")
    void update_whenKeywordIsBlank_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            domainKeyword.update(
                " ",
                "설명",
                DomainCompetencyCategory.SAFETY,
                BigDecimal.valueOf(4.0),
                BigDecimal.valueOf(2.0),
                true
            )
        );
    }

    @Test
    @DisplayName("Update domain keyword failure: throw exception when keyword length is out of range")
    void update_whenKeywordLengthIsOutOfRange_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            domainKeyword.update(
                "A",
                "설명",
                DomainCompetencyCategory.SAFETY,
                BigDecimal.valueOf(4.0),
                BigDecimal.valueOf(2.0),
                true
            )
        );
    }

    @Test
    @DisplayName("Update domain keyword failure: throw exception when base score is negative")
    void update_whenBaseScoreIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            domainKeyword.update(
                "품질 개선",
                "설명",
                DomainCompetencyCategory.COLLABORATION,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(2.0),
                true
            )
        );
    }

    @Test
    @DisplayName("Update domain keyword failure: throw exception when weight is negative")
    void update_whenWeightIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            domainKeyword.update(
                "품질 개선",
                "설명",
                DomainCompetencyCategory.COLLABORATION,
                BigDecimal.valueOf(4.0),
                BigDecimal.valueOf(-0.1),
                true
            )
        );
    }

    @Test
    @DisplayName("Activate success: set domain keyword active")
    void activate_success() {
        domainKeyword.deactivate();
        domainKeyword.activate();

        assertTrue(domainKeyword.getDomainIsActive());
    }

    @Test
    @DisplayName("Deactivate success: set domain keyword inactive")
    void deactivate_success() {
        domainKeyword.deactivate();

        assertFalse(domainKeyword.getDomainIsActive());
    }
}

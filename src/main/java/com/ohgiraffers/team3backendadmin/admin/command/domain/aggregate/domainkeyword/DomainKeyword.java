package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "domain_keyword")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DomainKeyword {

    @Id
    @Column(name = "domain_keyword_id")
    private Long domainKeywordId;

    @Column(name = "domain_keyword", length = 50)
    private String domainKeyword;

    @Column(name = "domain_keyword_description")
    private String domainKeywordDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_competency_category")
    private DomainCompetencyCategory domainCompetencyCategory;

    @Column(name = "domain_base_score", precision = 10, scale = 2)
    private BigDecimal domainBaseScore;

    @Column(name = "domain_weight", precision = 10, scale = 2)
    private BigDecimal domainWeight;

    @Column(name = "domain_is_active")
    private Boolean domainIsActive;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    public void update(
        String domainKeyword,
        String domainKeywordDescription,
        DomainCompetencyCategory domainCompetencyCategory,
        BigDecimal domainBaseScore,
        BigDecimal domainWeight,
        Boolean domainIsActive
    ) {
        validateDomainKeyword(domainKeyword);
        validateNonNegative(domainBaseScore, "Domain base score");
        validateNonNegative(domainWeight, "Domain weight");

        this.domainKeyword = domainKeyword;
        this.domainKeywordDescription = domainKeywordDescription;
        this.domainCompetencyCategory = domainCompetencyCategory;
        this.domainBaseScore = domainBaseScore;
        this.domainWeight = domainWeight;
        this.domainIsActive = domainIsActive;
    }

    public void activate() {
        this.domainIsActive = true;
    }

    public void deactivate() {
        this.domainIsActive = false;
    }

    private void validateDomainKeyword(String domainKeyword) {
        if (domainKeyword == null || domainKeyword.isBlank()) {
            throw new IllegalArgumentException("Domain keyword must not be blank");
        }

        if (domainKeyword.length() < 2 || domainKeyword.length() > 50) {
            throw new IllegalArgumentException("Domain keyword must be between 2 and 50 characters");
        }
    }

    private void validateNonNegative(BigDecimal value, String fieldName) {
        if (value != null && value.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
    }
}

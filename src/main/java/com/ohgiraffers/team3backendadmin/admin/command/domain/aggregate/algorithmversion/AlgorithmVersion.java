package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.algorithmversion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

@Entity
@Table(name = "algorithm_version")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AlgorithmVersion {

    @Id
    @Column(name = "algorithm_version_id")
    private Long algorithmVersionId;

    @Column(name = "version_no", nullable = false, length = 50)
    private String versionNo;

    @Column(name = "implementation_key", nullable = false, length = 100)
    private String implementationKey;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "policy_config", columnDefinition = "json")
    private String policyConfig;

    @Column(name = "is_active")
    private Boolean isActive;

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
        String versionNo,
        String implementationKey,
        String description,
        String policyConfig,
        Boolean isActive
    ) {
        validateVersionNo(versionNo);
        validateImplementationKey(implementationKey);
        validateDescription(description);

        this.versionNo = versionNo;
        this.implementationKey = implementationKey;
        this.description = description;
        this.policyConfig = normalizePolicyConfig(policyConfig);
        this.isActive = isActive;
    }

    private void validateVersionNo(String versionNo) {
        if (versionNo == null || versionNo.isBlank()) {
            throw new IllegalArgumentException("Algorithm version number must not be blank");
        }
        if (versionNo.length() > 50) {
            throw new IllegalArgumentException("Algorithm version number must be 50 characters or less");
        }
    }

    private void validateImplementationKey(String implementationKey) {
        if (implementationKey == null || implementationKey.isBlank()) {
            throw new IllegalArgumentException("Implementation key must not be blank");
        }
        if (implementationKey.length() > 100) {
            throw new IllegalArgumentException("Implementation key must be 100 characters or less");
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description must be 500 characters or less");
        }
    }

    private String normalizePolicyConfig(String policyConfig) {
        return policyConfig == null || policyConfig.isBlank() ? null : policyConfig;
    }
}

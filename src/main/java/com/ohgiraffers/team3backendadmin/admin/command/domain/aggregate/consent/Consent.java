package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.consent;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consent")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Consent {

    @Id
    @Column(name = "consent_id")
    private Long consentId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "consent_version", length = 10)
    private String consentVersion;

    @Column(name = "consent_saved_path")
    private String consentSavedPath;

    @Column(name = "is_agreed")
    private Boolean isAgreed;

    @Column(name = "consented_at")
    private LocalDate consentedAt;

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
}

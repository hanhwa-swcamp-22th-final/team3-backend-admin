package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.ocsaweightconfig;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "OCSA_weight_config")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OCSAWeightConfig {

    @Id
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "industry_preset_name")
    private String industryPresetName;

    @Column(name = "weight_v1", precision = 3, scale = 2)
    private BigDecimal weightV1;

    @Column(name = "weight_v2", precision = 3, scale = 2)
    private BigDecimal weightV2;

    @Column(name = "weight_v3", precision = 3, scale = 2)
    private BigDecimal weightV3;

    @Column(name = "weight_v4", precision = 3, scale = 2)
    private BigDecimal weightV4;

    @Column(name = "alpha_weight", precision = 2, scale = 1)
    private BigDecimal alphaWeight;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

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

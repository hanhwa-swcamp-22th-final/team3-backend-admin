package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "environment_standard")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class EnvironmentStandard {

    @Id
    @Column(name = "environment_standard_id")
    private Long environmentStandardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment_type", nullable = false)
    private EnvironmentType environmentType;

    @Column(name = "environment_code", nullable = false)
    private String environmentCode;

    @Column(name = "environment_name", nullable = false)
    private String environmentName;

    @Column(name = "env_temp_min", nullable = false)
    private BigDecimal envTempMin;

    @Column(name = "env_temp_max", nullable = false)
    private BigDecimal envTempMax;

    @Column(name = "env_humidity_min", nullable = false)
    private BigDecimal envHumidityMin;

    @Column(name = "env_humidity_max", nullable = false)
    private BigDecimal envHumidityMax;

    @Column(name = "env_particle_limit", nullable = false)
    private Integer envParticleLimit;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

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

    public EnvironmentStandard(Long environmentStandardId,
                               EnvironmentType environmentType,
                               String environmentCode,
                               String environmentName,
                               BigDecimal envTempMin,
                               BigDecimal envTempMax,
                               BigDecimal envHumidityMin,
                               BigDecimal envHumidityMax,
                               Integer envParticleLimit) {
        this(environmentStandardId, environmentType, environmentCode, environmentName, envTempMin, envTempMax,
            envHumidityMin, envHumidityMax, envParticleLimit, false, null, null, null, null);
    }

    @Builder
    public EnvironmentStandard(Long environmentStandardId,
                               EnvironmentType environmentType,
                               String environmentCode,
                               String environmentName,
                               BigDecimal envTempMin,
                               BigDecimal envTempMax,
                               BigDecimal envHumidityMin,
                               BigDecimal envHumidityMax,
                               Integer envParticleLimit,
                               Boolean isDeleted,
                               LocalDateTime createdAt,
                               Long createdBy,
                               LocalDateTime updatedAt,
                               Long updatedBy) {
        this.environmentStandardId = environmentStandardId;
        this.environmentType = environmentType;
        this.environmentCode = environmentCode;
        this.environmentName = environmentName;
        this.envTempMin = envTempMin;
        this.envTempMax = envTempMax;
        this.envHumidityMin = envHumidityMin;
        this.envHumidityMax = envHumidityMax;
        this.envParticleLimit = envParticleLimit;
        this.isDeleted = isDeleted == null ? false : isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public void updateInfo(EnvironmentType environmentType,
                           String environmentCode,
                           String environmentName,
                           BigDecimal envTempMin,
                           BigDecimal envTempMax,
                           BigDecimal envHumidityMin,
                           BigDecimal envHumidityMax,
                           Integer envParticleLimit) {
        validate(environmentType, environmentCode, environmentName, envTempMin, envTempMax,
            envHumidityMin, envHumidityMax, envParticleLimit);

        this.environmentType = environmentType;
        this.environmentCode = environmentCode;
        this.environmentName = environmentName;
        this.envTempMin = envTempMin;
        this.envTempMax = envTempMax;
        this.envHumidityMin = envHumidityMin;
        this.envHumidityMax = envHumidityMax;
        this.envParticleLimit = envParticleLimit;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    private void validate(EnvironmentType environmentType,
                          String environmentCode,
                          String environmentName,
                          BigDecimal envTempMin,
                          BigDecimal envTempMax,
                          BigDecimal envHumidityMin,
                          BigDecimal envHumidityMax,
                          Integer envParticleLimit) {
        if (environmentType == null) {
            throw new IllegalArgumentException("Environment type must not be null.");
        }
        if (environmentCode == null || environmentCode.isBlank()) {
            throw new IllegalArgumentException("Environment code must not be blank.");
        }
        if (environmentName == null || environmentName.isBlank()) {
            throw new IllegalArgumentException("Environment name must not be blank.");
        }
        if (envTempMin == null || envTempMax == null) {
            throw new IllegalArgumentException("Temperature range must not be null.");
        }
        if (envTempMin.compareTo(envTempMax) > 0) {
            throw new IllegalArgumentException("Minimum temperature must be less than or equal to maximum temperature.");
        }
        if (envHumidityMin == null || envHumidityMax == null) {
            throw new IllegalArgumentException("Humidity range must not be null.");
        }
        if (envHumidityMin.compareTo(envHumidityMax) > 0) {
            throw new IllegalArgumentException("Minimum humidity must be less than or equal to maximum humidity.");
        }
        if (envParticleLimit == null) {
            throw new IllegalArgumentException("Particle limit must not be null.");
        }
    }
}
package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "environment_standard")
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
    private String enviromentCode;

    @Column(name = "environment_name", nullable = false)
    private String enviromentName;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    public EnvironmentStandard(Long environmentStandardId,
                               EnvironmentType environmentType,
                               String enviromentCode,
                               String enviromentName,
                               BigDecimal envTempMin,
                               BigDecimal envTempMax,
                               BigDecimal envHumidityMin,
                               BigDecimal envHumidityMax,
                               Integer envParticleLimit) {
        this(environmentStandardId, environmentType, enviromentCode, enviromentName, envTempMin, envTempMax,
            envHumidityMin, envHumidityMax, envParticleLimit, null, null, null, null);
    }

    @Builder
    public EnvironmentStandard(Long environmentStandardId,
                               EnvironmentType environmentType,
                               String enviromentCode,
                               String enviromentName,
                               BigDecimal envTempMin,
                               BigDecimal envTempMax,
                               BigDecimal envHumidityMin,
                               BigDecimal envHumidityMax,
                               Integer envParticleLimit,
                               LocalDateTime createdAt,
                               Long createdBy,
                               LocalDateTime updatedAt,
                               Long updatedBy) {
        this.environmentStandardId = environmentStandardId;
        this.environmentType = environmentType;
        this.enviromentCode = enviromentCode;
        this.enviromentName = enviromentName;
        this.envTempMin = envTempMin;
        this.envTempMax = envTempMax;
        this.envHumidityMin = envHumidityMin;
        this.envHumidityMax = envHumidityMax;
        this.envParticleLimit = envParticleLimit;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
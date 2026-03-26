package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    public EnvironmentStandard(Long environmentStandardId,
                               EnvironmentType environmentType,
                               BigDecimal envTempMin,
                               BigDecimal envTempMax,
                               BigDecimal envHumidityMin,
                               BigDecimal envHumidityMax,
                               Integer envParticleLimit) {
        this.environmentStandardId = environmentStandardId;
        this.environmentType = environmentType;
        this.envTempMin = envTempMin;
        this.envTempMax = envTempMax;
        this.envHumidityMin = envHumidityMin;
        this.envHumidityMax = envHumidityMax;
        this.envParticleLimit = envParticleLimit;
    }
}

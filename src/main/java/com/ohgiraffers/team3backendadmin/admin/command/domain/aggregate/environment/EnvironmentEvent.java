package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment;

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
@Table(name = "environment_event")
@Getter
@NoArgsConstructor
public class EnvironmentEvent {

    @Id
    @Column(name = "environment_event_id")
    private Long environmentEventId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "env_temperature")
    private BigDecimal envTemperature;

    @Column(name = "env_humidity")
    private BigDecimal envHumidity;

    @Column(name = "env_particle_cnt")
    private Integer envParticleCnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "env_deviation_type", nullable = false)
    private EnvDeviationType envDeviationType;

    @Column(name = "env_correction_applied")
    private Boolean envCorrectionApplied;

    @Column(name = "env_detected_at", nullable = false)
    private LocalDateTime envDetectedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder
    public EnvironmentEvent(Long environmentEventId,
                            Long equipmentId,
                            BigDecimal envTemperature,
                            BigDecimal envHumidity,
                            Integer envParticleCnt,
                            EnvDeviationType envDeviationType,
                            Boolean envCorrectionApplied,
                            LocalDateTime envDetectedAt,
                            LocalDateTime createdAt,
                            Long createdBy,
                            LocalDateTime updatedAt,
                            Long updatedBy) {
        this.environmentEventId = environmentEventId;
        this.equipmentId = equipmentId;
        this.envTemperature = envTemperature;
        this.envHumidity = envHumidity;
        this.envParticleCnt = envParticleCnt;
        this.envDeviationType = envDeviationType;
        this.envCorrectionApplied = envCorrectionApplied;
        this.envDetectedAt = envDetectedAt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
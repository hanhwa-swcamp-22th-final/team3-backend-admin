package com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLogSnapshotEvent {

    private Long maintenanceLogId;
    private Long equipmentId;
    private Long maintenanceItemStandardId;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private BigDecimal maintenanceScore;
    private BigDecimal etaMaintDelta;
    private String maintenanceResult;
    private Boolean deleted;
    private LocalDateTime occurredAt;
}

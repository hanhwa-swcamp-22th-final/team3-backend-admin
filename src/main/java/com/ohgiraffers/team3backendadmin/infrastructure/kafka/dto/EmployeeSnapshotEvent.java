package com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSnapshotEvent {

    private Long employeeId;
    private String employeeCode;
    private String employeeTier;
    private String employeeStatus;
    private LocalDateTime occurredAt;
}

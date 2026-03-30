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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker_deployment")
@Getter
@NoArgsConstructor
public class WorkerDeployment {

    @Id
    @Column(name = "worker_deployment_id")
    private Long workerDeploymentId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "worker_deployment_role", nullable = false)
    private WorkerDeploymentRole workerDeploymentRole;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false)
    private Shift shift;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder
    public WorkerDeployment(Long workerDeploymentId,
                            Long employeeId,
                            Long equipmentId,
                            WorkerDeploymentRole workerDeploymentRole,
                            LocalDate startDate,
                            LocalDate endDate,
                            Shift shift,
                            LocalDateTime createdAt,
                            Long createdBy,
                            LocalDateTime updatedAt,
                            Long updatedBy) {
        this.workerDeploymentId = workerDeploymentId;
        this.employeeId = employeeId;
        this.equipmentId = equipmentId;
        this.workerDeploymentRole = workerDeploymentRole;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shift = shift;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
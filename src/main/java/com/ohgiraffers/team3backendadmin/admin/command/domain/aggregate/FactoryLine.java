package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "factory_line")
@Getter
@NoArgsConstructor
public class FactoryLine {

    @Id
    @Column(name = "factory_line_id")
    private Long factoryLineId;

    @Column(name = "factory_line_code", nullable = false, unique = true)
    private String factoryLineCode;

    @Column(name = "factory_line_name", nullable = false)
    private String factoryLineName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    public FactoryLine(Long factoryLineId, String factoryLineCode, String factoryLineName) {
        this(factoryLineId, factoryLineCode, factoryLineName, null, null, null, null);
    }

    @Builder
    public FactoryLine(Long factoryLineId,
                       String factoryLineCode,
                       String factoryLineName,
                       LocalDateTime createdAt,
                       Long createdBy,
                       LocalDateTime updatedAt,
                       Long updatedBy) {
        this.factoryLineId = factoryLineId;
        this.factoryLineCode = factoryLineCode;
        this.factoryLineName = factoryLineName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
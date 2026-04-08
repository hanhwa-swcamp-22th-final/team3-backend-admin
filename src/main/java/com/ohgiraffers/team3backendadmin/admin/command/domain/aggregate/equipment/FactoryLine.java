package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "factory_line")
@EntityListeners(AuditingEntityListener.class)
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

    public FactoryLine(Long factoryLineId, String factoryLineCode, String factoryLineName) {
        this(factoryLineId, factoryLineCode, factoryLineName, false, null, null, null, null);
    }

    @Builder
    public FactoryLine(Long factoryLineId,
                       String factoryLineCode,
                       String factoryLineName,
                       Boolean isDeleted,
                       LocalDateTime createdAt,
                       Long createdBy,
                       LocalDateTime updatedAt,
                       Long updatedBy) {
        this.factoryLineId = factoryLineId;
        this.factoryLineCode = factoryLineCode;
        this.factoryLineName = factoryLineName;
        this.isDeleted = isDeleted == null ? false : isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public void updateInfo(String factoryLineCode, String factoryLineName){
      if (factoryLineCode == null  || factoryLineCode.isBlank()
          || factoryLineName == null || factoryLineName.isBlank()){
        throw new IllegalArgumentException("Factory line code and name must not be null or blank.");
      }

      this.factoryLineCode = factoryLineCode;
      this.factoryLineName = factoryLineName;
    }

    public void softDelete(){
      this.isDeleted = true;
    }
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "department")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @Column(name = "department_name", length = 30)
    private String departmentName;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "depth")
    private String depth;

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

    public void updateNames(String departmentName, String teamName) {
        if (departmentName != null) {
            this.departmentName = departmentName;
        }
        if (teamName != null) {
            this.teamName = teamName;
        }
    }

    public void softDelete() {
        this.departmentName = "삭제됨";
        this.teamName = "삭제됨";
        this.parentDepartmentId = null;
        this.depth = null;
    }
}

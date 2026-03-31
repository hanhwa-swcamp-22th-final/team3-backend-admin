package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.rolechangehistory;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_change_history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoleChangeHistory {

    @Id
    @Column(name = "role_change_history_id")
    private Long roleChangeHistoryId;

    @Column(name = "target_employee_id", nullable = false)
    private Long targetEmployeeId;

    @Column(name = "changed_by", nullable = false)
    private Long changedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_role")
    private EmployeeRole previousRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_role")
    private EmployeeRole newRole;

    @Column(name = "reason")
    private String reason;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
}

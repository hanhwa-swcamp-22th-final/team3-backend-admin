package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_email", unique = true)
    private String employeeEmail;

    @Column(name = "employee_phone", unique = true)
    private String employeePhone;

    @Column(name = "employee_address")
    private String employeeAddress;

    @Column(name = "employee_emergency_contact", length = 30)
    private String employeeEmergencyContact;

    @Column(name = "employee_password")
    private String employeePassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_role")
    private EmployeeRole employeeRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_status")
    private EmployeeStatus employeeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_tier")
    private EmployeeTier employeeTier;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Builder.Default
    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled = false;

    @Builder.Default
    @Column(name = "login_fail_count")
    private Integer loginFailCount = 0;

    @Builder.Default
    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

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

    public void updatePersonalInfo(String name, String email, String phone,
                                   String address, String emergencyContact) {
        if (name != null) this.employeeName = name;
        if (email != null) this.employeeEmail = email;
        if (phone != null) this.employeePhone = phone;
        if (address != null) this.employeeAddress = address;
        if (emergencyContact != null) this.employeeEmergencyContact = emergencyContact;
    }

    public void updateRole(EmployeeRole newRole) {
        this.employeeRole = newRole;
    }

    public void updateTier(EmployeeTier newTier) {
        this.employeeTier = newTier;
    }

    public void changePassword(String encodedPassword) {
        this.employeePassword = encodedPassword;
    }

    public void assignDepartment(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void deleteEmployee() {
        this.employeeStatus = EmployeeStatus.ON_LEAVE;
    }
}

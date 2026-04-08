package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateResponse {
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private String employeeAddress;
    private String employeeEmergencyContact;
}

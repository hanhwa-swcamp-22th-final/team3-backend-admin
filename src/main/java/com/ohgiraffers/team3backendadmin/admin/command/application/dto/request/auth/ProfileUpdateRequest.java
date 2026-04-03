package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileUpdateRequest {

    private final String employeeName;

    private final String employeeEmail;

    private final String employeePhone;

    private final String employeeAddress;

    private final String employeeEmergencyContact;
}

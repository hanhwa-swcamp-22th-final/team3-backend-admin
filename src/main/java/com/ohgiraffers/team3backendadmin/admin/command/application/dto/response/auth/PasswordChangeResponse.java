package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeResponse {
    private String currentPassword;
    private String newPassword;
}

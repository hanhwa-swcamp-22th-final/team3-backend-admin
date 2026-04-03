package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordChangeRequest {

    @NotBlank
    private final String currentPassword;

    @NotBlank
    private final String newPassword;
}

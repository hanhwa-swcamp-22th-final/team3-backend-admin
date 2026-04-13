package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmPolicyReviewRequest {

    @NotBlank(message = "policyConfig is required.")
    private String policyConfig;
}
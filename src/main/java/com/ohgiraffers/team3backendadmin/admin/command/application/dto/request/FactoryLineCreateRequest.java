package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryLineCreateRequest {

    private String factoryLineCode;
    private String factoryLineName;
}
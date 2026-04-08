package com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactoryLineSearchRequest {

    private String keyword;
}



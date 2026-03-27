package com.ohgiraffers.team3backendadmin.admin.query.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EquipmentQueryServiceTest {

    @Test
    @DisplayName("Get equipment list success: return a list response DTO")
    void getEquipments_success() {
    }

    @Test
    @DisplayName("Get equipment list failure: return an empty list when there is no data")
    void getEquipments_whenNoData_thenReturnEmptyList() {
    }

    @Test
    @DisplayName("Get equipment detail success: return a detail response DTO")
    void getEquipmentDetail_success() {
    }

    @Test
    @DisplayName("Get equipment detail failure: throw an exception when equipment is not found")
    void getEquipmentDetail_whenEquipmentNotFound_thenThrow() {
    }
}
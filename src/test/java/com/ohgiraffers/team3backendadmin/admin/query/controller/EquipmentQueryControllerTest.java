package com.ohgiraffers.team3backendadmin.admin.query.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
class EquipmentQueryControllerTest {

    @Test
    @DisplayName("Get equipment list API success: return list JSON")
    void getEquipments_success() {
    }

    @Test
    @DisplayName("Get equipment list API success: bind query parameters correctly")
    void getEquipments_withQueryParams_success() {
    }

    @Test
    @DisplayName("Get equipment detail API success: return detail JSON")
    void getEquipmentDetail_success() {
    }

    @Test
    @DisplayName("Get equipment detail API failure: return 404 when the target does not exist")
    void getEquipmentDetail_whenServiceThrowsNotFound_thenNotFound() {
    }
}
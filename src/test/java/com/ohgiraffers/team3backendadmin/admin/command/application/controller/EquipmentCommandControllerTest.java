package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
class EquipmentCommandControllerTest {

    @Test
    @DisplayName("설비 등록 API 성공: 정상 응답을 반환한다")
    void createEquipment_success() {
    }

    @Test
    @DisplayName("설비 등록 API 실패: 잘못된 요청이면 400을 반환한다")
    void createEquipment_whenInvalidRequest_thenBadRequest() {
    }

    @Test
    @DisplayName("설비 수정 API 성공: 정상 응답을 반환한다")
    void updateEquipment_success() {
    }

    @Test
    @DisplayName("설비 수정 API 실패: 대상이 없으면 404를 반환한다")
    void updateEquipment_whenServiceThrowsNotFound_thenNotFound() {
    }

    @Test
    @DisplayName("설비 삭제 API 성공: 정상 응답을 반환한다")
    void deleteEquipment_success() {
    }
}

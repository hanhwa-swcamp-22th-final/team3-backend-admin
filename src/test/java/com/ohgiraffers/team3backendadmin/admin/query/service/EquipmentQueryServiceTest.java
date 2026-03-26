package com.ohgiraffers.team3backendadmin.admin.query.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EquipmentQueryServiceTest {

    @Test
    @DisplayName("설비 목록 조회 성공: 목록 응답 DTO를 반환한다")
    void getEquipments_success() {
    }

    @Test
    @DisplayName("설비 목록 조회 실패: 데이터가 없으면 빈 목록을 반환한다")
    void getEquipments_whenNoData_thenReturnEmptyList() {
    }

    @Test
    @DisplayName("설비 상세 조회 성공: 상세 응답 DTO를 반환한다")
    void getEquipmentDetail_success() {
    }

    @Test
    @DisplayName("설비 상세 조회 실패: 설비가 없으면 예외가 발생한다")
    void getEquipmentDetail_whenEquipmentNotFound_thenThrow() {
    }
}

package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EquipmentQueryMapperTest {

    @Test
    @DisplayName("설비 목록 조회 성공: 전체 목록을 조회한다")
    void selectEquipmentList_success() {
    }

    @Test
    @DisplayName("설비 목록 조회 성공: 키워드 조건이 반영된다")
    void selectEquipmentList_withKeyword_success() {
    }

    @Test
    @DisplayName("설비 목록 조회 성공: 상태 필터가 반영된다")
    void selectEquipmentList_withStatusFilter_success() {
    }

    @Test
    @DisplayName("설비 상세 조회 성공: 설비 상세를 조회한다")
    void selectEquipmentDetailById_success() {
    }

    @Test
    @DisplayName("설비 상세 조회 실패: 존재하지 않는 ID면 null을 반환한다")
    void selectEquipmentDetailById_whenUnknownId_thenNull() {
    }
}

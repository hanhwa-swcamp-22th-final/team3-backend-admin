package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import jakarta.persistence.Entity;

public enum EquipmentStatus {
  OPERATING,         // 가동 중
  STOPPED,           // 중지
  UNDER_INSPECTION,  // 점검 중
  DISPOSED           // 폐기
}

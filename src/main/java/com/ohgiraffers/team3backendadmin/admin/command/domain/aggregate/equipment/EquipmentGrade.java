package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import jakarta.persistence.Entity;

public enum EquipmentGrade {
  S, // 최상위 등급
  A, // 상위 등급
  B, // 중간 등급
  C  // 기본 등급
}

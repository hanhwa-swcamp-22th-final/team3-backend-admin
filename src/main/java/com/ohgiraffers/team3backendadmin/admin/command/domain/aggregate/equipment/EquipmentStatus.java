package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import jakarta.persistence.Entity;

public enum EquipmentStatus {
  OPERATING,
  STOPPED,
  UNDER_INSPECTION,
  DISPOSED
}

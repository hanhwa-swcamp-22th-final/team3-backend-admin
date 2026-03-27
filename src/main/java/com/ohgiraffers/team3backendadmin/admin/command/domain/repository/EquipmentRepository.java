package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEquipmentRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaEquipmentRepository {

  Optional<Equipment> findByEquipmentCode(String equipmentCode);
}
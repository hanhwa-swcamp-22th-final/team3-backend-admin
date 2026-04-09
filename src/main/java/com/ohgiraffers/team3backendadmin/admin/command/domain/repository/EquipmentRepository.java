package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEquipmentRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaEquipmentRepository {

  Optional<Equipment> findByEquipmentCode(String equipmentCode);

  List<Equipment> findByEnvironmentStandardId(Long environmentStandardId);
}

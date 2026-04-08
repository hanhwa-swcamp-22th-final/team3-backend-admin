package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaEquipmentProcessRepository extends JpaRepository<EquipmentProcess, Long> {
    Optional<EquipmentProcess> findByEquipmentProcessCode(String equipmentProcessCode);
}

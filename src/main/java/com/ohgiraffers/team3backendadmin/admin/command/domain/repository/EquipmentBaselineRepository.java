package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEquipmentBaselineRepository;
import java.util.Optional;

public interface EquipmentBaselineRepository extends JpaEquipmentBaselineRepository {

    Optional<EquipmentBaseline> findByEquipmentId(Long equipmentId);
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEquipmentAgingParamRepository;
import java.util.Optional;

public interface EquipmentAgingParamRepository extends JpaEquipmentAgingParamRepository {

    Optional<EquipmentAgingParam> findByEquipmentId(Long equipmentId);
}

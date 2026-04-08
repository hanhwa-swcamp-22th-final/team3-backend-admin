package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEquipmentAgingParamRepository extends JpaRepository<EquipmentAgingParam, Long> {
}
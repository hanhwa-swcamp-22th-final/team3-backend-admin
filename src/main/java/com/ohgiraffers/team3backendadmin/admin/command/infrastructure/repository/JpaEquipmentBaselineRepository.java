package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentBaseline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEquipmentBaselineRepository extends JpaRepository<EquipmentBaseline, Long> {
}
package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEquipmentProcessRepository extends JpaRepository<EquipmentProcess, Long> {
}

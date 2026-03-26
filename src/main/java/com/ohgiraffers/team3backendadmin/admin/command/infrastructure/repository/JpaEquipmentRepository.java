package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEquipmentRepository extends JpaRepository<Equipment, Long> {

}
package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaFactoryLineRepository extends JpaRepository<FactoryLine, Long> {
    Optional<FactoryLine> findByFactoryLineCode(String factoryLineCode);
}

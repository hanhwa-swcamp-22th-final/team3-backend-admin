package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFactoryLineRepository extends JpaRepository<FactoryLine, Long> {
}

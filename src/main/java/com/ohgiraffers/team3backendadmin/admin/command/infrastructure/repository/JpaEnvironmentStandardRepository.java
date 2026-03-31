package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEnvironmentStandardRepository extends JpaRepository<EnvironmentStandard, Long> {
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaEnvironmentStandardRepository;

import java.util.Optional;

public interface EnvironmentStandardRepository extends JpaEnvironmentStandardRepository {
    Optional<EnvironmentStandard> findByEnvironmentCode(String environmentCode);
}
package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEnvironmentEventRepository extends JpaRepository<EnvironmentEvent, Long> {
}
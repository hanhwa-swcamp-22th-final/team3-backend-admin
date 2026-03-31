package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.rolechangehistory.RoleChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleChangeHistoryRepository extends JpaRepository<RoleChangeHistory, Long> {
}

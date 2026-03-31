package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.passwordhistory.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
}

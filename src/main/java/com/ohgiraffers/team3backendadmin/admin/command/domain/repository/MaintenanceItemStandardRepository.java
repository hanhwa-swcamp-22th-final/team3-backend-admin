package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceItemStandardRepository extends JpaRepository<MaintenanceItemStandard, Long> {
}

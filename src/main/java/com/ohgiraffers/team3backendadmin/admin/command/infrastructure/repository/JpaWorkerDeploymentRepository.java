package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.workerdeployment.WorkerDeployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkerDeploymentRepository extends JpaRepository<WorkerDeployment, Long> {
}

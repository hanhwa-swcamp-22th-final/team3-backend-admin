package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.workerdeployment;

public enum WorkerDeploymentRole {
    PRIMARY,   // 주 담당
    ASSISTANT, // 보조
    TRAINING,  // 교육 중
    STANDBY    // 대기
}

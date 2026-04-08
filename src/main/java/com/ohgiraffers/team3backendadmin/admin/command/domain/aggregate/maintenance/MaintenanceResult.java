package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance;

public enum MaintenanceResult {
    NORMAL,            // 정상
    ANOMALY_DETECTED,  // 이상 감지
    REPAIR_REQUIRED,   // 수리 필요
    REPAIR_COMPLETED   // 수리 완료
}

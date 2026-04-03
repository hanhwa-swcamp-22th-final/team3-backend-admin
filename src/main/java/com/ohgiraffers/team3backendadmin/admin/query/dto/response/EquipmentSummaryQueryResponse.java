package com.ohgiraffers.team3backendadmin.admin.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentSummaryQueryResponse {

    private long totalCount;
    private long operatingCount;
    private long stoppedCount;
    private long underInspectionCount;
    private long disposedCount;
}

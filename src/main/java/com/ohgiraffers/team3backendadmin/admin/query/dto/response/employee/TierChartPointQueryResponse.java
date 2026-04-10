package com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TierChartPointQueryResponse {

    private Integer year;
    private Integer evalSequence;
    private String tier;
    private BigDecimal totalScore;
}

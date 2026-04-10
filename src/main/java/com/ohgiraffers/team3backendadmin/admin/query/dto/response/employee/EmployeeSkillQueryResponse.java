package com.ohgiraffers.team3backendadmin.admin.query.dto.response.employee;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeSkillQueryResponse {

    private Long skillId;
    private String skillName;
    private BigDecimal skillScore;
}

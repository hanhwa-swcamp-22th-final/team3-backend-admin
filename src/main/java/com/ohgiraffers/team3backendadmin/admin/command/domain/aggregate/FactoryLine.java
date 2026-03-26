package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factory_line")
@Getter
@NoArgsConstructor
public class FactoryLine {

    @Id
    @Column(name = "factory_line_id")
    private Long factoryLineId;

    @Column(name = "factory_line_code", nullable = false, unique = true)
    private String factoryLineCode;

    @Column(name = "factory_line_name", nullable = false)
    private String factoryLineName;

    public FactoryLine(Long factoryLineId, String factoryLineCode, String factoryLineName) {
        this.factoryLineId = factoryLineId;
        this.factoryLineCode = factoryLineCode;
        this.factoryLineName = factoryLineName;
    }
}

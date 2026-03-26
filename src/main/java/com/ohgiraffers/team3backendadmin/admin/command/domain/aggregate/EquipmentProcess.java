package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment_process")
@Getter
@NoArgsConstructor
public class EquipmentProcess {

    @Id
    @Column(name = "equipment_process_id")
    private Long equipmentProcessId;

    @Column(name = "factory_line_id", nullable = false)
    private Long factoryLineId;

    @Column(name = "equipment_process_code", nullable = false, unique = true)
    private String equipmentProcessCode;

    @Column(name = "equipment_process_name", nullable = false)
    private String equipmentProcessName;

    public EquipmentProcess(Long equipmentProcessId,
                            Long factoryLineId,
                            String equipmentProcessCode,
                            String equipmentProcessName) {
        this.equipmentProcessId = equipmentProcessId;
        this.factoryLineId = factoryLineId;
        this.equipmentProcessCode = equipmentProcessCode;
        this.equipmentProcessName = equipmentProcessName;
    }
}

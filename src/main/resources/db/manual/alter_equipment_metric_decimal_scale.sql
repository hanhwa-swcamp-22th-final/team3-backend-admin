ALTER TABLE equipment_aging_param
    MODIFY COLUMN equipment_eta_age DECIMAL(10, 4) NULL;

ALTER TABLE equipment_baseline
    MODIFY COLUMN equipment_standard_performance_rate DECIMAL(10, 4) NULL,
    MODIFY COLUMN equipment_baseline_error_rate DECIMAL(10, 4) NULL,
    MODIFY COLUMN equipment_eta_maint DECIMAL(10, 4) NULL,
    MODIFY COLUMN equipment_idx DECIMAL(10, 4) NULL;

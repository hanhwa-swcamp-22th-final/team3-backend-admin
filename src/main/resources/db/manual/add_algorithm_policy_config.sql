ALTER TABLE algorithm_version
    ADD COLUMN policy_config JSON NULL AFTER description;

ALTER TABLE batch_projection.evaluation_period_projection
    ADD COLUMN policy_config JSON NULL AFTER algorithm_implementation_key;

ALTER TABLE maintenance_item_standard
ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

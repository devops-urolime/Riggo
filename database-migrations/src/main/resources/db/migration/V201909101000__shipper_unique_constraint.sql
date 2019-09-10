-- ADD UNIQUE CONSTRAINT
ALTER TABLE load ALTER COLUMN expected_ship_date TYPE timestamp;
ALTER TABLE load ALTER COLUMN expected_delivery_date TYPE timestamp;

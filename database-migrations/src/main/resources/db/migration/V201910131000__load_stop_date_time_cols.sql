ALTER TABLE load_stop DROP COLUMN expected_date_time;
ALTER TABLE load_stop ADD COLUMN expected_date_time timestamp;
ALTER TABLE load_stop DROP COLUMN departure_date_time;
ALTER TABLE load_stop ADD COLUMN departure_date_time timestamp;


-- ADD UNIQUE CONSTRAINT
ALTER TABLE invoice DROP COLUMN arrival_date;
ALTER TABLE load_stop ADD COLUMN arrival_date timestamp;

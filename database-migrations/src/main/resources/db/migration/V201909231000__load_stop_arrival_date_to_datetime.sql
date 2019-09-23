-- ADD UNIQUE CONSTRAINT
ALTER TABLE load_stop DROP COLUMN arrival_date;
ALTER TABLE invoice ADD COLUMN arrival_date timestamp;

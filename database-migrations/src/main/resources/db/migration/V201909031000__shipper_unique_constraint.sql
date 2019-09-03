-- ADD UNIQUE CONSTRAINT
ALTER TABLE invoice DROP COLUMN quote_date;
ALTER TABLE invoice ADD COLUMN quote_date timestamp;

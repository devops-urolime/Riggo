-- add load_id to
DELETE FROM load_stop;
DELETE FROM load;

ALTER TABLE load ADD COLUMN "site_id" INTEGER NOT NULL REFERENCES site(id);
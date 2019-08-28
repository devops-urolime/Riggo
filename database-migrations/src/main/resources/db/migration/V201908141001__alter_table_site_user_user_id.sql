-- add load_id to
ALTER TABLE site_user RENAME COLUMN user_role TO user_id;
-- ALTER TABLE shipper ADD COLUMN site_id INTEGER REFERENCES site(id);

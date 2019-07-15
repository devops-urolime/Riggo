/** adds icon to menu schema and sets icon to name  LOWER(REPLACE(name, ' ', '-')) */
ALTER TABLE menu ADD COLUMN icon VARCHAR(32) DEFAULT NULL;

UPDATE menu SET icon = LOWER(REPLACE(name, ' ', '-'));


-- ADD UNIQUE CONSTRAINT
ALTER TABLE load_line_item DROP COLUMN haz_mat;
ALTER TABLE load_line_item ADD COLUMN haz_mat boolean;

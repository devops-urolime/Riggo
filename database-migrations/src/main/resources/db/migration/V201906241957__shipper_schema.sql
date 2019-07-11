/* Shipper Schema updates */
ALTER TABLE "shipper" DROP COLUMN first_name;
ALTER TABLE "shipper" DROP COLUMN last_name;
ALTER TABLE "shipper" ADD COLUMN name VARCHAR(256);
ALTER TABLE "shipper" ADD COLUMN site_id INTEGER REFERENCES site (id);
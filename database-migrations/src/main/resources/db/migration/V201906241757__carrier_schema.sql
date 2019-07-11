/* Carrier Schema updates */
ALTER TABLE "carrier" DROP COLUMN org_name;
ALTER TABLE "carrier" DROP COLUMN first_name;
ALTER TABLE "carrier" DROP COLUMN last_name;
ALTER TABLE "carrier" ADD COLUMN name VARCHAR(256);
ALTER TABLE "carrier" DROP COLUMN us_dot_number;

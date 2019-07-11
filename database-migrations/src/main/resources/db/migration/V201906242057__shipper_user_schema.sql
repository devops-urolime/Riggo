/* shipper_user Schema updates */
ALTER TABLE "shipper_user" DROP COLUMN site_id;
ALTER TABLE "shipper_user" DROP COLUMN carrier_id;
ALTER TABLE "shipper_user" ADD COLUMN user_id integer;
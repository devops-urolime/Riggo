/*201906211757*/

CREATE TRIGGER update_ts_shipper_user
BEFORE UPDATE ON shipper_user
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TRIGGER update_ts_location
BEFORE UPDATE ON location
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TRIGGER update_ts_shipper_location
BEFORE UPDATE ON shipper_location
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();


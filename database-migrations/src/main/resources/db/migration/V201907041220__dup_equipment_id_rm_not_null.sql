ALTER TABLE "public"."load"
  ALTER COLUMN "shipper_id" DROP NOT NULL,
  ALTER COLUMN "expected_ship_date" DROP NOT NULL,
  ALTER COLUMN "carrier" DROP NOT NULL,
  ALTER COLUMN "transport_mode" DROP NOT NULL,
  ALTER COLUMN "posted_rate" DROP NOT NULL,
  ALTER COLUMN "posted_currency" DROP NOT NULL,
  ALTER COLUMN "load_status" DROP NOT NULL,
  ALTER COLUMN "expected_delivery_date" DROP NOT NULL,
  ALTER COLUMN "driver" DROP NOT NULL;
  
  ALTER TABLE "public"."address"
  ALTER COLUMN "address_1" DROP NOT NULL,
  ALTER COLUMN "city" DROP NOT NULL,
  ALTER COLUMN "state" DROP NOT NULL,
  ALTER COLUMN "country" DROP NOT NULL,
  ALTER COLUMN "postal_code" DROP NOT NULL;
  
ALTER TABLE "public"."equipment_type" ALTER COLUMN "type" DROP NOT NULL;

  

ALTER TABLE "public"."load_stop"
  ALTER COLUMN "type" DROP NOT NULL,
  ALTER COLUMN "stop_number" DROP NOT NULL;
 
ALTER TABLE "public"."load_stop"
  ALTER COLUMN "type" DROP NOT NULL,
  ALTER COLUMN "stop_number" DROP NOT NULL;


ALTER TABLE "public"."location" ALTER COLUMN "name" DROP NOT NULL;
;

ALTER TABLE "public"."trucker"
  ALTER COLUMN "first_name" DROP NOT NULL,
  ALTER COLUMN "last_name" DROP NOT NULL;

ALTER TABLE "public"."load" RENAME COLUMN "equipment_type" TO "equipment_type_id";





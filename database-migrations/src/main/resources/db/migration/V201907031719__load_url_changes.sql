ALTER TABLE "public"."load"
--  DROP COLUMN "dispatcher",
  DROP COLUMN "equipment_type_id";


ALTER TABLE "public"."load" ALTER COLUMN "load_url" TYPE character varying(255);

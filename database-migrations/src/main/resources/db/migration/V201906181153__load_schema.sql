ALTER TABLE "public"."load" RENAME COLUMN "riggoh_status" TO "load_status";
ALTER TABLE "public"."load" ADD COLUMN reference_number VARCHAR(64);
ALTER TABLE "public"."load" ADD COLUMN equipment_type_id INTEGER;

COMMENT ON COLUMN load.reference_number IS 'Shipper Reference Number';


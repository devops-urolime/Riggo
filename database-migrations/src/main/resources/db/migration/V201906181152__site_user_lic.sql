
ALTER TABLE "public"."site" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."site" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";

-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
/* 
* drop columns and add new ones in a good place 
* we can do it now. Later this wont make sense as we will have data
*/
ALTER TABLE "public"."site"
  DROP COLUMN "created_at",
  DROP COLUMN "updated_at",
  DROP COLUMN "deleted",
  ADD COLUMN "api_key" varchar(150),
  ADD COLUMN "auth_client_id" varchar(60),
  ADD COLUMN "auth_clent_secret" varchar(60),
  ADD COLUMN "created_at" TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  ADD COLUMN "updated_at" TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  ADD COLUMN "deleted" TIMESTAMPTZ  DEFAULT NULL;
 



/*
201907021130*/

ALTER TABLE "public"."location" ADD COLUMN "ext_sys_tenant_id" varchar(18);
ALTER TABLE "public"."load_stop" ADD COLUMN "ext_sys_tenant_id" varchar(18);
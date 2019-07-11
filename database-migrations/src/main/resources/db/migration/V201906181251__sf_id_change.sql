select 'load';

ALTER TABLE "public"."load" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."load" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";

select 'phone';

ALTER TABLE "public"."phone" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."phone" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";

select 'carrier';

ALTER TABLE "public"."carrier" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."carrier" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";


select 'customer shipper';


ALTER TABLE "public"."customer_shipper" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."customer_shipper" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";



select 'customer address';

ALTER TABLE "public"."address" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."address" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";


select 'customer trucker';
ALTER TABLE "public"."trucker" RENAME COLUMN "sf_id" TO "ext_sys_id";
ALTER TABLE "public"."trucker" RENAME COLUMN "sf_tenant_id" TO "ext_sys_tenant_id";


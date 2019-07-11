
ALTER TABLE "public"."site_license" ALTER COLUMN "license_key" TYPE character varying(100);

ALTER TABLE "public"."site" ALTER COLUMN "api_key" TYPE character varying(150);

ALTER TABLE "public"."site" ALTER COLUMN "auth_clent_secret" TYPE character varying(150);

ALTER TABLE "public"."site_license" RENAME COLUMN "license_id" TO "license_type";

ALTER TABLE "public"."site_license" ALTER COLUMN "license_type" TYPE varchar(150);


INSERT INTO public.site (id, ext_sys_id, ext_sys_tenant_id, site_name, domain_name, api_key, auth_client_id, auth_clent_secret, created_at, updated_at, deleted) VALUES (110, '100', '10', 'poc', 'poc.com', 'cf202aaa-4978-487e-96b0-0dedae81787b', 'vEIxYbs6lyul3JOGDaNa5LIuFfCru3ij', 'CU7TnHUl6e_khgWsNYo9Vbp58HhjS2TKXRyGf8lnwt5-28520pQApQR-RyXUNfeB', '2019-06-20 15:35:41.173004+05:30', '2019-06-20 16:15:23.414984+05:30', NULL);
INSERT INTO public.site (id, ext_sys_id, ext_sys_tenant_id, site_name, domain_name, api_key, auth_client_id, auth_clent_secret, created_at, updated_at, deleted) VALUES (100, '100', '10', 'SF_REVONOVA', 'revonova.com', '95dbcbda-9db5-48bd-aad3-4a950eee8393', 'W8784VHyGeYRj8xC4nXL7HEtiTDaDTEp', 'ZkVEZ6wSeWni9kEcRcNbeuTPWM1b_iVwYyCAFp8anQ_n4KNINaEvoDnD0jHa3lYe', '2019-06-20 15:30:09.252898+05:30', '2019-06-20 16:15:23.414984+05:30', NULL);
INSERT INTO public.site (id, ext_sys_id, ext_sys_tenant_id, site_name, domain_name, api_key, auth_client_id, auth_clent_secret, created_at, updated_at, deleted) VALUES (150, '100', '10', 'Carrier', 'carrier.poc.com', '5125a20a-9de9-4605-b72e-8e51d019aae5', NULL, NULL, '2019-06-20 16:15:23.414984+05:30', '2019-06-20 16:15:40.012386+05:30', NULL);


--
-- Data for Name: site_license; Type: TABLE DATA; Schema: public; Owner: riggo_db_dev
--

INSERT INTO public.site_license (site_id, license_type, license_key, domain_name, created_at, updated_at, deleted) VALUES (100, 'SALESFORCE_REVENOVA_API_INTEGRATION', '4b11cd52-8414-4e72-acf0-db2c46472e7c', 'revonova.com', '2019-06-20 16:18:17.540257+05:30', '2019-06-20 16:18:17.540257+05:30', NULL);
INSERT INTO public.site_license (site_id, license_type, license_key, domain_name, created_at, updated_at, deleted) VALUES (150, 'CLIENT_APP_API_INTEGRATION', 'f6beb242-03d3-4b2d-924d-a54a9f39cd65', 'poc.cm', '2019-06-20 16:18:17.540257+05:30', '2019-06-20 16:18:17.540257+05:30', NULL);


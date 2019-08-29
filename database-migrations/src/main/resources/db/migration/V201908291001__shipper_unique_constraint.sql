-- ADD UNIQUE CONSTRAINT
ALTER TABLE shipper ADD CONSTRAINT unique_shipper_ext_sys_id_site_id UNIQUE (ext_sys_id, site_id);
-- load.ext_sys_tenantid -> load.ext_sys_tenant_id
ALTER TABLE load_line_item RENAME COLUMN ext_sys_tenantid to ext_sys_tenant_id;

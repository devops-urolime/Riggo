alter table load_line_item add column ext_sys_id varchar(18);
alter table load_line_item add column ext_sys_tenantid varchar(20);
alter table load_line_item add column load_id INTEGER NOT NULL;
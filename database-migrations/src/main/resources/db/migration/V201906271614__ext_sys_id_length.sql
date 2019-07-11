
alter table address alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);
alter table carrier alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);
alter table equipment_type alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);

alter table load alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);
alter table load_stop alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);
alter table location alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);

alter table phone alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);
alter table shipper alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);

alter table trucker alter column ext_sys_id type varchar(18) using ext_sys_id::varchar(18);







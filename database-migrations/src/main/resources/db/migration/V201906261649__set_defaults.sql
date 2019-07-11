alter table load alter column transport_mode type varchar(100) using transport_mode::varchar(100);

/* always 1 
 USD */

alter table load alter column posted_currency set default 1;

alter table load alter column insurnace_currency set default 1;

alter table load alter column load_status set default 0; /*Unassigned */





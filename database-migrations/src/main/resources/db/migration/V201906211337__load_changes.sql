Drop table carrier_address;
Drop table carrier_phone;
Drop table customer_load;
alter table customer_shipper RENAME TO shipper;
Drop table customer_shipper_address;
alter table load rename customer_id 
    to shipper_id;
alter table load ADD CONSTRAINT load_shipper_fk  
FOREIGN KEY (shipper_id) REFERENCES shipper (id);

 

Drop table shipper_phone;

Create table shipper_user(
id   SERIAL PRIMARY KEY,
shipper_id integer REFERENCES shipper(id),
site_id integer REFERENCES site(id) ,
carrier_id integer REFERENCES carrier(id),
created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
deleted TIMESTAMPTZ  DEFAULT NULL
);



Drop table trucker_address;

Drop table trucker_phone;

Create table location (
    id   SERIAL PRIMARY KEY,
    location_name varchar(100) not null,
    address_id  integer REFERENCES address (id) ,
    ext_sys_id varchar(16),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL

);

Alter table load_stop drop column address_id;

Alter table load_stop add column location_id integer;

Alter table load_stop ADD CONSTRAINT location_load_stop_fk  
FOREIGN KEY (location_id) REFERENCES location (id); 

Alter table load_stop drop column name;

Create table shipper_location(
    id   SERIAL PRIMARY KEY,
    shipper_id integer references shipper(id),
    location_id integer references location(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
)  
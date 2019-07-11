
/*
PGSQL Convention: underscored objects 
tables:tbl_(object type ) + _riggo_mp (Read as:: riggo market place) + _object

start object with object type  add prodct prefix for tables for all object that

*/

CREATE OR REPLACE FUNCTION set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE IF NOT EXISTS site ( 
    id SERIAL PRIMARY KEY,
    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    site_name VARCHAR(30),
    
    domain_name VARCHAR(60) NOT NULL,


    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 
);
CREATE  TRIGGER update_ts_site
    BEFORE UPDATE ON site
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();



CREATE TABLE IF NOT EXISTS site_license ( 

    site_id INTEGER  NOT NULL references site(id)  ,
    license_id INTEGER NOT NULL, 
    license_key VARCHAR(80) NOT NULL,
    domain_name VARCHAR(60) NOT NULL,


    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL,
    PRIMARY KEY (license_id,site_id) 
);
CREATE TRIGGER update_ts_site_license
    BEFORE UPDATE ON site_license
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS phone ( 
    id SERIAL PRIMARY KEY,
    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    phone VARCHAR(15),
    is_primary boolean default '0',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 
);

CREATE TRIGGER update_ts_phone
    BEFORE UPDATE ON phone
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();


CREATE TABLE IF NOT EXISTS address ( 
    id SERIAL PRIMARY KEY,
    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    address_1 VARCHAR(60) NOT NULL,
    address_2 VARCHAR(30),
    city    INTEGER NOT NULL,
    state   INTEGER NOT NULL,
    country INTEGER NOT NULL default '1',
    postal_code VARCHAR(50) NOT NULL,
    is_primary boolean default '0',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 
);
CREATE TRIGGER trg_riggo_mp_update_ts_address
    BEFORE UPDATE ON address
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();



    /** END MASTERS */

CREATE TABLE IF NOT EXISTS users ( 
    id SERIAL PRIMARY KEY,
    email VARCHAR(60) NOT NULL,
    password VARCHAR(70) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
        deleted TIMESTAMPTZ  DEFAULT NULL
);

CREATE TRIGGER update_ts_users
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();



CREATE TABLE IF NOT EXISTS site_user ( 
    user_id   INTEGER NOT NULL REFERENCES users(id),
    site_id   INTEGER NOT NULL REFERENCES site(id),
    user_role INTEGER NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL,
    PRIMARY KEY (user_id,site_id) 
);

CREATE TRIGGER update_ts_site_user
    BEFORE UPDATE ON site_user
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS customer_shipper(
    id SERIAL  PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL

 );
CREATE TRIGGER customer_shipper
BEFORE UPDATE ON customer_shipper
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS customer_shipper_address(
    customer_id INTEGER REFERENCES customer_shipper(id),
    address_id INTEGER REFERENCES address(id),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
    
);
CREATE TRIGGER update_ts_customer_shipper_address
BEFORE UPDATE ON customer_shipper_address
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();


CREATE TABLE IF NOT EXISTS shipper_phone(
    customer_id INTEGER REFERENCES customer_shipper(id),
    phone_id INTEGER REFERENCES phone(id),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL

);
CREATE TRIGGER shipper_phone
BEFORE UPDATE ON shipper_phone
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();


CREATE TABLE IF NOT EXISTS carrier(
    id SERIAL  PRIMARY KEY,
    org_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    
    us_dot_number VARCHAR(25) NOT NULL,

    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    /*billing_address_id REFERENCES tbl_riggo_mp_address(address_id), */
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 );

CREATE TRIGGER ts_carrier
BEFORE UPDATE ON carrier
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS carrier_address(
    carrier_id INTEGER REFERENCES carrier(id),
    address_id INTEGER REFERENCES address(id),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
    
);


CREATE TRIGGER update_ts_carrier_address
BEFORE UPDATE ON carrier_address
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS carrier_phone ( 

    phone_id SERIAL PRIMARY KEY,
    carrier_id INTEGER REFERENCES carrier(id),

    phone VARCHAR(15),
    is_primary boolean default '0',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 
);

CREATE TRIGGER update_ts_carrier_phone
    BEFORE UPDATE ON carrier_phone
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();


CREATE TABLE IF NOT EXISTS trucker ( 
id SERIAL PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
sf_id VARCHAR(16),
sf_tenant_id VARCHAR(20),
last_name VARCHAR(50) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
 updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
);

CREATE TRIGGER update_ts_trucker
    BEFORE UPDATE ON trucker
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();


 CREATE TABLE IF NOT EXISTS trucker_address(
    trucker_id INTEGER REFERENCES trucker(id),
    address_id INTEGER REFERENCES address(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
    
);


CREATE TRIGGER update_ts_trucker_address
BEFORE UPDATE ON trucker_address
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();

CREATE TABLE IF NOT EXISTS trucker_phone ( 

    phone_id SERIAL PRIMARY KEY,
    trucker_id INTEGER REFERENCES trucker(id),
    is_primary boolean default '0',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
 
);

CREATE TRIGGER update_ts_trucker_phone
    BEFORE UPDATE ON trucker_phone
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();   


/* loads */
CREATE TABLE IF NOT EXISTS load(
    id SERIAL  PRIMARY KEY,
    sf_id VARCHAR(16),
    sf_tenant_id VARCHAR(20),
    distance_miles DECIMAL(7,3),
    customer_id INTEGER NOT NULL,
    expected_ship_date DATE NOT NULL,
    origin INTEGER NOT NULL,
    destination INTEGER NOT NULL,
    carrier INTEGER NOT NULL,
    transport_mode SMALLINT NOT NULL,
    posted_rate DECIMAL (6,2) NOT NULL,
    posted_currency SMALLINT NOT NULL,
    insurance_amt DECIMAL(6,2),
    insurnace_currency SMALLINT ,
    total_weight  DECIMAL(6,2),
    weight_uom    SMALLINT , 
    equipment_type INTEGER,
    riggoh_status INTEGER NOT NULL,
    team_req BOOLEAN,
    food_grade_trailer_req BOOLEAN,
    temp_control_req BOOLEAN,
    expected_delivery_date date NOT NULL,
    driver INTEGER NOT NULL,
    dispatcher INTEGER,
    location_based_svcs_req BOOLEAN,

     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL

    );

    CREATE TRIGGER update_ts_load
    BEFORE UPDATE ON load
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();



CREATE TABLE IF NOT EXISTS customer_load(
    customer_id INTEGER REFERENCES customer_shipper(id),
    loads_id INTEGER REFERENCES load(id),
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
    
);



CREATE TRIGGER update_ts_trucker_customer_load
BEFORE UPDATE ON customer_load
FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();
DROP TABLE IF EXISTS invoice;
CREATE TABLE IF NOT EXISTS invoice (
    id SERIAL PRIMARY KEY,
    ext_sys_id VARCHAR(18),
    load_id INTEGER NOT NULL,
    quote_date TIME,
    status INTEGER NOT NULL,
    comments VARCHAR(256),
    net_freight_charges NUMERIC (6,2),
    fuel_surcharge NUMERIC (6,2),
    accessorial_charges NUMERIC (6,2),
    transportation_total NUMERIC (6,2),
    customer_quote_total NUMERIC (6,2),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
);


CREATE  TRIGGER update_ts_invoice
     BEFORE UPDATE ON invoice
     FOR EACH ROW
     EXECUTE PROCEDURE set_timestamp();


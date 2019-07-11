
/** load_stop - holds stop information on a load.  */
CREATE TABLE IF NOT EXISTS load_stop (
    id SERIAL PRIMARY KEY,
    ext_sys_id VARCHAR(18),
    name VARCHAR(100),
    type INTEGER NOT NULL,
    stop_number INTEGER NOT NULL default 0,
    address_id INTEGER REFERENCES address (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
);

COMMENT ON TABLE load_stop IS 'Load Stops';
COMMENT ON COLUMN load_stop.type IS 'PICKUP, STOP, DESTINATION';
COMMENT ON COLUMN load_stop.stop_number IS 'Stop Sequence Number - Relevant to type STOP only';

CREATE TRIGGER load_stop_updated_at
    BEFORE UPDATE ON load_stop
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();
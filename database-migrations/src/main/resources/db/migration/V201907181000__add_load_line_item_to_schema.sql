/** load_line_item schema.  */
CREATE TABLE IF NOT EXISTS load_line_item (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(100),
                                         description VARCHAR(100),
                                         pickup_stop INTEGER,  -- REFERENCES load_stop.id
                                         delivery_stop INTEGER, -- REFERENCES load_stop.id
                                         weight NUMERIC (6,2),
                                         haz_mat BIT,
                                         rank INTEGER NOT NULL,
                                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         deleted TIMESTAMPTZ  DEFAULT NULL
);


CREATE TRIGGER load_line_item_updated_at
    BEFORE UPDATE ON load_line_item
    FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();
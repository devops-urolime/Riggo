/** menu schema.  */
CREATE TABLE IF NOT EXISTS menu (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(100),
                                         type INTEGER NOT NULL DEFAULT 0,
                                         site_id INTEGER REFERENCES site (id),
                                         parent_menu_id INTEGER NOT NULL DEFAULT 0,
                                         url VARCHAR(256) NOT NULL,
                                         rank INTEGER NOT NULL,
                                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         deleted TIMESTAMPTZ  DEFAULT NULL
);

COMMENT ON TABLE menu IS 'Menu';
COMMENT ON COLUMN menu.type IS 'LEFT_HAND_MENU';
COMMENT ON COLUMN menu.parent_menu_id IS 'Nested Menu Support - 0 if Root Menu';

CREATE TRIGGER menu_updated_at
    BEFORE UPDATE ON menu
    FOR EACH ROW
EXECUTE PROCEDURE set_timestamp();
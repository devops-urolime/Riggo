/**
    Update Load Schema
    This patch updates the schema to handle the payload emitted from Salesforce.
    The changes are meant to reflect the latest payload from salesforce,
    as well as the latest mocks in figma.
*/

/* Change riggoh_status to load_status.  This field takes rtms__Load_Status__c */
/** equipment_type - which holds stop information on a load.  */
CREATE TABLE IF NOT EXISTS equipment_type (
    id SERIAL PRIMARY KEY,
    ext_sys_id VARCHAR(18),
    site_id INTEGER REFERENCES site (id),
    type INTEGER NOT NULL,
    name VARCHAR(32),
    rank INTEGER DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted TIMESTAMPTZ  DEFAULT NULL
);

COMMENT ON TABLE equipment_type IS 'Equipment Types By Site';
COMMENT ON COLUMN equipment_type.type IS 'VAN(1), FLATBED(2), REEFER(3)';
COMMENT ON COLUMN load_stop.type IS 'PICKUP, STOP, DESTINATION';

CREATE TRIGGER equipment_type_updated_at
    BEFORE UPDATE ON load_stop
    FOR EACH ROW
    EXECUTE PROCEDURE set_timestamp();



/** INSERT DATA FOR ALL EQUIPMENT TYPES CURRENTLY DEFINED */
INSERT INTO equipment_type (ext_sys_id, site_id, type, name, rank)
    VALUES
        ('a0V6A0000020EUs',100, 1,'Dry Van 48''', 0),
        ('a0V6A0000020EUt',100, 1,'Dry Van 45''', 0),
        ('a0V6A0000020EUu',100, 1,'Dry Van 43''', 0),
        ('a0V6A0000020EUv',100, 1,'Dry Van 42''', 0),
        ('a0V6A0000020EUw',100, 1,'Dry Van 40''', 0),
        ('a0V6A0000020EUx',100, 1,'Dry Van/Pup 28''', 0),
        ('a0V6A0000020EV3',100, 1,'Straight Truck 26''', 0),
        ('a0V6A0000020EV4',100, 1,'Straight Truck 25''', 0),
        ('a0V6A0000020EV5',100, 1,'Straight Truck 24''', 0),
        ('a0V6A0000020EV6',100, 1,'Trailer on Flat Car / Piggyback', 0),
        ('a0V6A0000020EVC',100, 1,'Open Top Trailer 48''', 0),
        ('a0V6A0000020EVD',100, 1,'Curtain Side 53''', 0),
        ('a0V6A0000020EVE',100, 1,'Curtain Side 48''', 0),
        ('a0V6A0000043kyW',100, 1,'Reefer / Dry Van ''48', 0),
        ('a0V6A0000043l8N',100, 1,'Reefer / Dry Van ''53', 0),
        ('a0V6A00000468d3',100, 1,'Dry Van 53''', 0),
        ('a0V6A0000020EVF',100, 2,'Flatbed 2 Axle 48''', 0),
        ('a0V6A0000020EVG',100, 2,'Flatbed 3 Axle 53''', 0),
        ('a0V6A0000020EUr',100, 3,'Reefer 53''', 0),
        ('a0V6A0000020EUz',100, 3,'Reefer 51''', 0),
        ('a0V6A0000020EV0',100, 3,'Reefer 48''', 0),
        ('a0V6A0000020EV1',100, 3,'Reefer 23''', 0),
        ('a0V6A0000020EV2',100, 3,'Reefer 36''', 0);




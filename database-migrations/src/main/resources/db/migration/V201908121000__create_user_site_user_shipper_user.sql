-- INSERT users
INSERT INTO users(id, email, created_at)
    VALUES
        (1,'edward+shipperadmin@riggo.io', now()),
        (2,'ismael+shipperadmin@riggo.io', now());


-- INSERT site_user
INSERT INTO site_user(site_id, user_role, first_name, last_name, created_at)
	VALUES
	    (100, 1, 'Edward', 'Song', now()),
	    (100, 2, 'Ismael', 'Terreno', now());


-- INSERT INTO shipper
-- INSERT INTO shipper (id, ext_sys_id, name, site_id)
--     VALUES
--         (1, '001g000001ywr1BAAQ', 'DK Shipper', 100),
--         (2, '0016A00000XsyTsQAJ', 'Boston Market c/o England Logistics', 100);


-- INSERT INTO shipper_user
INSERT INTO shipper_user(shipper_id, created_at, user_id)
    VALUES
        (1, now(), 1),
        (2, now(), 2);

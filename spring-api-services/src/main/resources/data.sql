/* menu */
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Shipments', 1, 100, 0, '', 2);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Dashboard', 1, 100, 0, '', 1);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Insights', 1, 100, 0, '', 4);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Billing', 1, 100, 0, '', 3);
/* end of menu */


/* load */
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (1, 'extSysId1', 100, 1, 0, 0, 1);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (2, 'extSysId2', 100, 1, 0, 0, 2);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (3, 'extSysId3', 100, 1, 0, 0, 3);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (4, 'extSysId4', 100, 1, 0, 0, 4);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (5, 'extSysId5', 100, 1, 0, 0, 5);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (6, 'extSysId6', 100, 1, 0, 0, 6);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (7, 'extSysId7', 100, 1, 0, 0, 7);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status) VALUES (8, 'extSysId8', 100, 1, 0, 0, 8);


/* load_line_item */
INSERT INTO load_line_item(id, load_id) VALUES (1, 1);
INSERT INTO load_line_item(id, load_id) VALUES (2, 1);


/* invoice */
INSERT INTO invoice(id, ext_sys_id) VALUES (1, 'extSysId1');


/* shipper */
INSERT INTO shipper(id, ext_sys_id, site_id) VALUES (1, 'extSysId1', 100);


/* user */
INSERT INTO users(id, email) VALUES (1, 'email@riggo.io');


/* site_user */
INSERT INTO site_user (id, site_id, user_id) VALUES (1, 100, 1);


/* shipper_user */
INSERT INTO shipper_user (id, shipper_id, user_id) VALUES (1, 1, 1);

/* site */
INSERT INTO site(id) VALUES (100);


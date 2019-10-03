/* menu */
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Shipments', 1, 100, 0, '', 2);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Dashboard', 1, 100, 0, '', 1);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Insights', 1, 100, 0, '', 4);
INSERT INTO menu(name, type, site_id, parent_menu_id, url, rank) VALUES ('Billing', 1, 100, 0, '', 3);
/* end of menu */


/* load */
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (1, 'extSysId1', 100, 1, 0, 0, 1, '2019-08-01', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (2, 'extSysId2', 100, 1, 0, 0, 2, '2019-08-02', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (3, 'extSysId3', 100, 1, 0, 0, 3, '2019-08-03', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (4, 'extSysId4', 100, 1, 0, 0, 4, '2019-08-04', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (5, 'extSysId5', 100, 1, 0, 0, 5, '2019-08-05', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (6, 'extSysId6', 100, 1, 0, 0, 6, '2019-08-06', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (7, 'extSysId7', 100, 1, 0, 0, 7, '2019-08-07', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (8, 'extSysId8', 100, 1, 0, 0, 8, '2019-08-08', 12.12);
INSERT INTO load(id, ext_sys_id, site_id, shipper_id, carrier, driver, load_status, expected_delivery_date, distance_miles) VALUES (9, 'extSysId8', 100, 1, 0, 0, 9, '2019-08-09', 12.12);


/* load_line_item */
INSERT INTO load_line_item(id, load_id, ext_sys_id) VALUES (1, 1, 'extSysId1');
INSERT INTO load_line_item(id, load_id, ext_sys_id) VALUES (2, 1, 'extSysId2');

/* load_stop */
INSERT INTO load_stop(id, load_id, arrival_status, type, ext_sys_id) VALUES (1, 1, 1, 1, 'extSysId1');
INSERT INTO load_stop(id, load_id, arrival_status, type, ext_sys_id) VALUES (2, 1, 2, 2, 'extSysId2');


/* invoice */
INSERT INTO quote(id, ext_sys_id, load_id, quote_date, status) VALUES (1, 'extSysId1', 7, '2019-07-15 00:00:00', 3);


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

/* fiscal_period */
INSERT INTO fiscal_period(
	date_dim_id, date_actual, epoch, day_suffix, day_name, day_of_week, day_of_month,
	day_of_quarter, day_of_year, week_of_month, week_of_year, week_of_year_iso,
	month_actual, month_name, month_name_abbreviated, quarter_actual, quarter_name,
	year_actual, first_day_of_week, last_day_of_week, first_day_of_month,
	last_day_of_month, first_day_of_quarter, last_day_of_quarter,
	first_day_of_year, last_day_of_year, mmyyyy, mmddyyyy, weekend_indr)
	VALUES
	(20190821, '2019-08-21', 1566345600, '21st', 'Wednesday', 3, 21,
	52, 233, 3, 34, '2019-W34-3', 8, 'August', 'Aug', 3, 'Third', 2019,
	'2019-08-19', '2019-08-25', '2019-08-01',
	'2019-08-31', '2019-07-01', '2019-09-30',
	'2019-01-01', '2019-12-31', '082019', '08212019', false
	),
	(20190921, '2019-09-21', 1569024000, '21st', 'Saturday', 6, 21,
	83, 264, 3, 38, '2019-W38-6', 9, 'September', 'Sep', 3, 'Third', 2019,
	'2019-09-16', '2019-09-22', '2019-09-01',
	'2019-09-30', '2019-07-01', '2019-09-30',
	'2019-01-01', '2019-12-31', '092019', '09212019', true
	);



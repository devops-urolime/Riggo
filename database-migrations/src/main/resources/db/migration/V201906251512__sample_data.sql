/* Sample data updates */
INSERT INTO public.shipper(
    id, ext_sys_id, ext_sys_tenant_id, created_at, name, site_id)
VALUES (1, '', '', now(),  'Hello Shipping', 100);

INSERT INTO public.load(
    ext_sys_id, ext_sys_tenant_id, distance_miles, shipper_id, expected_ship_date, origin, destination, carrier, transport_mode, posted_rate, posted_currency, insurance_amt, insurnace_currency, total_weight, weight_uom, equipment_type, load_status, team_req, food_grade_trailer_req, temp_control_req, expected_delivery_date, driver, dispatcher, location_based_svcs_req, created_at, deleted, reference_number, equipment_type_id)
VALUES ('20000', 'ext_sys_tenant_id', 1212, 1, '2019-06-27 12:00:00', 1, 1, 1, 1, 12.21, 1, 1000, 1, 5000, 1, 1, 1, true, true, true, '2019-07-01 08:00:00', 1, 1, true, now(), null, 'reference number', 1);

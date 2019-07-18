/** adds icon to menu schema and sets icon to name  LOWER(REPLACE(name, ' ', '-')) */
ALTER TABLE load DROP COLUMN total_weight;
ALTER TABLE load DROP COLUMN distance_miles;
ALTER TABLE load DROP COLUMN insurnace_currency;
ALTER TABLE load DROP COLUMN posted_currency;
ALTER TABLE load DROP COLUMN weight_uom;
ALTER TABLE load DROP COLUMN on_time_delivery_counter;
ALTER TABLE load DROP COLUMN delivery_status;
ALTER TABLE load DROP COLUMN distance_kilometers;
ALTER TABLE load DROP COLUMN margin_invoiced;
ALTER TABLE load DROP COLUMN margin_piad;
ALTER TABLE load DROP COLUMN margin_pct_invoiced;
ALTER TABLE load DROP COLUMN sales_schedule_status;
ALTER TABLE load DROP COLUMN load_shipping_status;
ALTER TABLE load DROP COLUMN pickup_delivery_number;
ALTER TABLE load DROP COLUMN stop_reference_number;
ALTER TABLE load DROP COLUMN site_url;


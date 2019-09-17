-- load.invoice_total -> load.customer_invoice_total AND drop load.customer_transportation_total
ALTER TABLE load RENAME COLUMN invoice_total to customer_invoice_total;
ALTER TABLE load DROP COLUMN customer_transport_total;
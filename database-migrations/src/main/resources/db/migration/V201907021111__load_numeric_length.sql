/*201907021111*/

ALTER TABLE "public"."load"
  ALTER COLUMN "distance_miles" TYPE numeric(9,2),
  ALTER COLUMN "posted_rate" TYPE numeric(9,2),
  ALTER COLUMN "insurance_amt" TYPE numeric(9,2),
  ALTER COLUMN "total_weight" TYPE numeric(9,2),
  ALTER COLUMN "invoice_total" TYPE numeric(9,2),
  ALTER COLUMN "carrier_quote_total" TYPE numeric(9,2),
  ALTER COLUMN "carrier_invoice_total" TYPE numeric(9,2),
  ALTER COLUMN "customer_quote_total" TYPE numeric(9,2),
  ALTER COLUMN "customer_transport_total" TYPE numeric(9,2),
  ALTER COLUMN "distance_kilometers" TYPE numeric(9,2),
  ALTER COLUMN "margin_invoiced" TYPE numeric(9,2),
  ALTER COLUMN "margin_piad" TYPE numeric(9,2),
  ALTER COLUMN "margin_pct_invoiced" TYPE numeric(6,2);

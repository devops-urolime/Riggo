ALTER TABLE "public"."load_stop" ADD COLUMN load_id INTEGER REFERENCES load (id);
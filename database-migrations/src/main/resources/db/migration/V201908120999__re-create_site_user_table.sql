DROP TABLE IF EXISTS site_user;
CREATE TABLE IF NOT EXISTS site_user
(
    id SERIAL PRIMARY KEY,
    site_id integer NOT NULL,
    user_role integer NOT NULL,
    first_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    deleted timestamp with time zone
);

CREATE  TRIGGER update_ts_site_user
     BEFORE UPDATE ON site_user
     FOR EACH ROW
     EXECUTE PROCEDURE set_timestamp();


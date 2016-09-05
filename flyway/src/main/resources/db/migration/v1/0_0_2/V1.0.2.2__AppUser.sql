CREATE TABLE app_user (
    id bigint NOT NULL PRIMARY KEY,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    user_name character varying(255) NOT NULL UNIQUE
);

CREATE SEQUENCE app_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
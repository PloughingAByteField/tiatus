CREATE TABLE race (
    id bigint NOT NULL PRIMARY KEY,
    active boolean,
    closed boolean,
    draw_locked boolean,
    name character varying(255) UNIQUE,
    race_order integer,
    start_time character varying(255)
);

CREATE SEQUENCE race_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
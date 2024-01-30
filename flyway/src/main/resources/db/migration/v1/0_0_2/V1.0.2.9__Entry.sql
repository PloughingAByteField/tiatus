CREATE TABLE entry (
    id bigint NOT NULL PRIMARY KEY,
    crew character varying(255),
    is_fixed_number boolean,
    number integer,
    race_order integer,
    time_only boolean,
    weighting character varying(255),
    event_id bigint NOT NULL REFERENCES event (id),
    race_id bigint NOT NULL REFERENCES race (id)
);

CREATE SEQUENCE entry_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
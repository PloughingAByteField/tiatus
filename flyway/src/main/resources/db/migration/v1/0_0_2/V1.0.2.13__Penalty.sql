CREATE TABLE penalty (
    id bigint NOT NULL PRIMARY KEY,
    comment character varying(255),
    note character varying(255),
    "time" timestamp without time zone,
    entry_id bigint NOT NULL REFERENCES entry (id)
);

CREATE SEQUENCE penalty_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
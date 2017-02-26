CREATE TABLE disqualification (
    id bigint NOT NULL PRIMARY KEY,
    comment character varying(255),
    note character varying(255),
    entry_id bigint NOT NULL REFERENCES entry (id)
);

CREATE SEQUENCE disqualification_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
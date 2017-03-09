CREATE TABLE position (
    id bigint NOT NULL PRIMARY KEY,
    name character varying(255) UNIQUE
);

CREATE SEQUENCE position_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
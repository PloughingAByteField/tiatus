CREATE TABLE event (
    id bigint NOT NULL PRIMARY KEY,
    is_weighted boolean,
    name character varying(255) UNIQUE
);

CREATE SEQUENCE event_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE TABLE club (
    id bigint NOT NULL PRIMARY KEY,
    club_name character varying(255) UNIQUE
);

CREATE SEQUENCE club_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
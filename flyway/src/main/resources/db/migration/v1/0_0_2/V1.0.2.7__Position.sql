CREATE TABLE position (
    id bigint NOT NULL PRIMARY KEY,
    active boolean,
    name character varying(255) UNIQUE,
    showAllEntries boolean,
    positionOrder int NOT NULL,
    timing boolean,
    can_start boolean
);

ALTER TABLE position ADD CONSTRAINT position_constraint UNIQUE (timing, positionOrder);


CREATE SEQUENCE position_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
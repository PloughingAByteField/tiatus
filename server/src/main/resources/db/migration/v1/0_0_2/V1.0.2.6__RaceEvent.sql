CREATE TABLE race_event (
    id bigint NOT NULL PRIMARY KEY,
    race_id bigint NOT NULL REFERENCES race (id),
    event_id bigint NOT NULL REFERENCES event (id),
    race_event_order int NOT NULL
);

ALTER TABLE race_event ADD CONSTRAINT race_event_constraint UNIQUE (race_id, event_id);


CREATE SEQUENCE race_event_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
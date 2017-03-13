
CREATE TABLE event_position (
    id bigint NOT NULL PRIMARY KEY,
    position_order integer NOT NULL,
    event_id bigint NOT NULL REFERENCES event (id),
    position_id bigint NOT NULL REFERENCES position (id)
);

CREATE SEQUENCE event_position_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE event_position ADD CONSTRAINT event_position_order_constraint UNIQUE (event_id, position_id, position_order);
